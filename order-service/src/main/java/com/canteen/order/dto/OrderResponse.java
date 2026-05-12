package com.canteen.order.dto;

import com.canteen.order.entity.Order;
import com.canteen.order.entity.OrderItem;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long merchantId;
    private String status;
    private Integer totalPrice;
    private String pickupNumber;
    private String pickupCode;
    private List<OrderItem> items;
    private LocalDateTime createdAt;

    public static OrderResponse from(Order order, List<OrderItem> items) {
        OrderResponse r = new OrderResponse();
        r.id = order.getId();
        r.orderNo = order.getOrderNo();
        r.userId = order.getUserId();
        r.merchantId = order.getMerchantId();
        r.status = order.getStatus();
        r.totalPrice = order.getTotalPrice();
        r.pickupNumber = order.getPickupNumber();
        r.pickupCode = order.getPickupCode();
        r.items = items;
        r.createdAt = order.getCreatedAt();
        return r;
    }

    public Long getId() { return id; }
    public String getOrderNo() { return orderNo; }
    public Long getUserId() { return userId; }
    public Long getMerchantId() { return merchantId; }
    public String getStatus() { return status; }
    public Integer getTotalPrice() { return totalPrice; }
    public String getPickupNumber() { return pickupNumber; }
    public String getPickupCode() { return pickupCode; }
    public List<OrderItem> getItems() { return items; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
