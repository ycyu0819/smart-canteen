package com.canteen.order.controller;

import com.canteen.common.response.Result;
import com.canteen.order.dto.OrderResponse;
import com.canteen.order.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {
    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) { this.orderService = orderService; }

    @GetMapping("/orders")
    public Result<Page<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // 管理员查看所有订单 - 简化实现
        return Result.success(Page.empty());
    }
}
