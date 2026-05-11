package com.canteen.dish.controller;

import com.canteen.common.auth.UserContext;
import com.canteen.common.response.Result;
import com.canteen.dish.dto.DishRequest;
import com.canteen.dish.dto.DishResponse;
import com.canteen.dish.service.DishService;
import com.canteen.dish.service.StockService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dish")
public class DishController {
    private final DishService dishService;
    private final StockService stockService;

    public DishController(DishService dishService, StockService stockService) {
        this.dishService = dishService;
        this.stockService = stockService;
    }

    @PostMapping
    public Result<DishResponse> create(@Valid @RequestBody DishRequest req) {
        return Result.success(dishService.create(UserContext.getUserId(), req));
    }

    @PutMapping("/{id}")
    public Result<DishResponse> update(@PathVariable Long id, @Valid @RequestBody DishRequest req) {
        return Result.success(dishService.update(id, UserContext.getUserId(), req));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        dishService.delete(id, UserContext.getUserId());
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<DishResponse> getById(@PathVariable Long id) {
        return Result.success(dishService.getById(id));
    }

    @GetMapping("/list")
    public Result<List<DishResponse>> getMyDishes() {
        return Result.success(dishService.getMerchantDishes(UserContext.getUserId()));
    }

    @GetMapping("/available")
    public Result<List<DishResponse>> getAvailable(@RequestParam Long merchantId) {
        return Result.success(dishService.getAvailable(merchantId));
    }

    @PutMapping("/{id}/status")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        dishService.toggleStatus(id, UserContext.getUserId());
        return Result.success();
    }

    @PutMapping("/{id}/stock")
    public Result<Void> updateStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        stockService.manualUpdate(id, body.get("stock"));
        return Result.success();
    }

    // Internal APIs called by other services
    @PostMapping("/{id}/stock/deduct")
    public void deductStock(@PathVariable Long id,
                            @RequestParam int quantity,
                            @RequestParam Long orderId) {
        stockService.deduct(id, quantity, orderId);
    }

    @PostMapping("/{id}/stock/restore")
    public void restoreStock(@PathVariable Long id,
                             @RequestParam int quantity,
                             @RequestParam Long orderId) {
        stockService.restore(id, quantity, orderId);
    }
}
