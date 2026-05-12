package com.canteen.order.service;

import com.canteen.common.auth.UserContext;
import com.canteen.common.exception.BusinessException;
import com.canteen.common.response.ResultCode;
import com.canteen.order.client.DishClient;
import com.canteen.order.client.QueueClient;
import com.canteen.order.dto.CreateOrderRequest;
import com.canteen.order.dto.OrderItemRequest;
import com.canteen.order.dto.OrderResponse;
import com.canteen.order.entity.*;
import com.canteen.order.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final OrderStatusLogRepository statusLogRepo;
    private final DishClient dishClient;
    private final QueueClient queueClient;
    private final OrderTimeoutService timeoutService;

    public OrderService(OrderRepository orderRepo, OrderItemRepository orderItemRepo,
                        OrderStatusLogRepository statusLogRepo, DishClient dishClient,
                        QueueClient queueClient, OrderTimeoutService timeoutService) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.statusLogRepo = statusLogRepo;
        this.dishClient = dishClient;
        this.queueClient = queueClient;
        this.timeoutService = timeoutService;
    }

    @Transactional
    public OrderResponse create(Long userId, CreateOrderRequest req) {
        // 生成订单号
        String orderNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "A" + String.format("%05d", System.currentTimeMillis() % 100000);

        // 生成取餐码
        String pickupCode = String.format("%06d", new Random().nextInt(1000000));
        String pickupNumber = "A-" + String.format("%03d", (orderRepo.count() % 999) + 1);

        int totalPrice = 0;
        List<OrderItem> items = new ArrayList<>();

        // 扣库存 + 构建订单项
        for (OrderItemRequest it : req.getItems()) {
            // 通过 Feign 获取菜品信息（从 dish-service）
            dishClient.deductStock(it.getDishId(), it.getQuantity(), 0L);
            // 保存菜品快照（简化处理，实际应从 dish-service 获取）
            OrderItem item = new OrderItem();
            item.setDishId(it.getDishId());
            item.setDishName("Dish #" + it.getDishId());
            item.setUnitPrice(0); // 简化
            item.setQuantity(it.getQuantity());
            item.setSubtotal(0); // 简化
            items.add(item);
        }

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setMerchantId(req.getMerchantId());
        order.setStatus(OrderStatus.PLACED.name());
        order.setTotalPrice(totalPrice);
        order.setPickupNumber(pickupNumber);
        order.setPickupCode(pickupCode);
        order.setPaidAt(LocalDateTime.now()); // 模拟支付
        order = orderRepo.save(order);

        // 保存订单项
        for (OrderItem item : items) {
            item.setOrderId(order.getId());
            orderItemRepo.save(item);
        }

        // 记录状态日志
        StatusLog("SYSTEM", null, OrderStatus.PLACED.name(), "订单创建");

        // 设置超时自动取消（15分钟）
        timeoutService.scheduleTimeout(order.getId(), 15);

        return OrderResponse.from(order, items);
    }

    @Transactional
    public OrderResponse acceptOrder(Long orderId, Long merchantId) {
        Order order = getOrder(orderId);
        if (!order.getMerchantId().equals(merchantId)) {
            throw new BusinessException(400, "无权操作此订单");
        }
        transition(order, OrderStatus.ACCEPTED);
        order.setAcceptedAt(LocalDateTime.now());
        orderRepo.save(order);
        timeoutService.cancelTimeout(orderId); // 取消超时
        StatusLog("MERCHANT", merchantId, order.getStatus(), null);
        return getOrderResponse(order);
    }

    @Transactional
    public OrderResponse prepareOrder(Long orderId, Long merchantId) {
        Order order = getOrder(orderId);
        if (!order.getMerchantId().equals(merchantId))
            throw new BusinessException(400, "无权操作此订单");
        transition(order, OrderStatus.PREPARING);
        order.setPreparedAt(LocalDateTime.now());
        orderRepo.save(order);
        StatusLog("MERCHANT", merchantId, order.getStatus(), null);
        return getOrderResponse(order);
    }

    @Transactional
    public OrderResponse completeOrder(Long orderId, Long merchantId) {
        Order order = getOrder(orderId);
        if (!order.getMerchantId().equals(merchantId))
            throw new BusinessException(400, "无权操作此订单");
        transition(order, OrderStatus.WAITING);
        order.setCompletedAt(LocalDateTime.now());
        orderRepo.save(order);
        StatusLog("MERCHANT", merchantId, order.getStatus(), null);

        // 通知取餐排队服务
        queueClient.enqueue(Map.of(
                "orderId", order.getId(),
                "windowId", order.getMerchantId(),
                "pickupNumber", order.getPickupNumber(),
                "pickupCode", order.getPickupCode()));
        return getOrderResponse(order);
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userId, String reason) {
        Order order = getOrder(orderId);
        if (!order.getUserId().equals(userId))
            throw new BusinessException(400, "无权取消此订单");
        if (!OrderStatus.PLACED.name().equals(order.getStatus()))
            throw new BusinessException(ResultCode.INVALID_STATUS_TRANSITION);
        doCancel(order, reason, "USER");
    }

    @Transactional
    public void cancelByTimeout(Long orderId) {
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order == null || !OrderStatus.PLACED.name().equals(order.getStatus())) return;
        doCancel(order, "超时未处理", "SYSTEM");
    }

    public void pickedUp(Long orderId) {
        Order order = getOrder(orderId);
        order.setStatus(OrderStatus.PICKED_UP.name());
        orderRepo.save(order);
        StatusLog("SYSTEM", null, order.getStatus(), "窗口核销取餐");
    }

    private void doCancel(Order order, String reason, String operatorType) {
        order.setStatus(OrderStatus.CANCELLED.name());
        order.setCancelReason(reason);
        order.setCancelTime(LocalDateTime.now());
        order.setCancelledAt(LocalDateTime.now());
        orderRepo.save(order);
        // 恢复库存（通过 Feign）
        var items = orderItemRepo.findByOrderId(order.getId());
        for (OrderItem item : items) {
            dishClient.restoreStock(item.getDishId(), item.getQuantity(), order.getId());
        }
        StatusLog(operatorType, null, order.getStatus(), reason);
    }

    public OrderResponse getOrderDetail(Long orderId) {
        return getOrderResponse(getOrder(orderId));
    }

    public Page<OrderResponse> getUserOrders(Long userId, int page, int size) {
        return orderRepo.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size))
                .map(o -> getOrderResponse(o));
    }

    public Page<OrderResponse> getMerchantOrders(Long merchantId, int page, int size) {
        return orderRepo.findByMerchantIdOrderByCreatedAtDesc(merchantId, PageRequest.of(page, size))
                .map(o -> getOrderResponse(o));
    }

    public List<Map<String, Object>> getTimeline(Long orderId) {
        return statusLogRepo.findByOrderIdOrderByCreatedAtAsc(orderId)
                .stream().map(log -> Map.<String, Object>of(
                        "fromStatus", log.getFromStatus() != null ? log.getFromStatus() : "",
                        "toStatus", log.getToStatus(),
                        "operatorType", log.getOperatorType() != null ? log.getOperatorType() : "",
                        "createdAt", log.getCreatedAt().toString()))
                .toList();
    }

    private Order getOrder(Long orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new BusinessException(ResultCode.ORDER_NOT_FOUND));
    }

    private OrderResponse getOrderResponse(Order order) {
        return OrderResponse.from(order, orderItemRepo.findByOrderId(order.getId()));
    }

    private void transition(Order order, OrderStatus target) {
        OrderStatus current = OrderStatus.valueOf(order.getStatus());
        if (!current.canTransitionTo(target)) {
            throw new BusinessException(ResultCode.INVALID_STATUS_TRANSITION);
        }
        String from = order.getStatus();
        order.setStatus(target.name());
        StatusLog("MERCHANT", UserContext.getUserId(), from + "->" + target, null);
    }

    private void StatusLog(String operatorType, Long operatorId, String toStatus, String remark) {
        OrderStatusLog log = new OrderStatusLog();
        log.setToStatus(toStatus);
        log.setOperatorType(operatorType);
        log.setOperatorId(operatorId);
        log.setRemark(remark);
        statusLogRepo.save(log);
    }
}
