package com.canteen.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "dish-service")
public interface DishClient {

    @PostMapping("/api/dish/{id}/stock/deduct")
    void deductStock(@PathVariable Long id,
                     @RequestParam int quantity,
                     @RequestParam Long orderId);

    @PostMapping("/api/dish/{id}/stock/restore")
    void restoreStock(@PathVariable Long id,
                      @RequestParam int quantity,
                      @RequestParam Long orderId);

    @GetMapping("/api/dish/{id}")
    Object getDish(@PathVariable Long id);
}
