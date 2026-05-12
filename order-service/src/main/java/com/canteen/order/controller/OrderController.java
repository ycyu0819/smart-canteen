package com.canteen.order.controller;

import com.canteen.common.auth.UserContext;
import com.canteen.common.response.Result;
import com.canteen.order.dto.CreateOrderRequest;
import com.canteen.order.dto.OrderResponse;
import com.canteen.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping
    public Result<OrderResponse> create(@Valid @RequestBody CreateOrderRequest req) {
        return Result.success(orderService.create(UserContext.getUserId(), req));
    }

    @GetMapping("/my")
    public Result<Page<OrderResponse>> myOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(orderService.getUserOrders(UserContext.getUserId(), page, size));
    }

    @GetMapping("/merchant")
    public Result<Page<OrderResponse>> merchantOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(orderService.getMerchantOrders(UserContext.getUserId(), page, size));
    }

    @GetMapping("/{id}")
    public Result<OrderResponse> getDetail(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(id));
    }

    @PutMapping("/{id}/accept")
    public Result<OrderResponse> accept(@PathVariable Long id) {
        return Result.success(orderService.acceptOrder(id, UserContext.getUserId()));
    }

    @PutMapping("/{id}/prepare")
    public Result<OrderResponse> prepare(@PathVariable Long id) {
        return Result.success(orderService.prepareOrder(id, UserContext.getUserId()));
    }

    @PutMapping("/{id}/complete")
    public Result<OrderResponse> complete(@PathVariable Long id) {
        return Result.success(orderService.completeOrder(id, UserContext.getUserId()));
    }

    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id, @RequestParam(defaultValue = "用户取消") String reason) {
        orderService.cancelOrder(id, UserContext.getUserId(), reason);
        return Result.success();
    }

    @GetMapping("/{id}/timeline")
    public Result<List<Map<String, Object>>> timeline(@PathVariable Long id) {
        return Result.success(orderService.getTimeline(id));
    }

    @PutMapping("/{id}/picked-up")
    public void pickedUp(@PathVariable Long id) {
        orderService.pickedUp(id);
    }
}
