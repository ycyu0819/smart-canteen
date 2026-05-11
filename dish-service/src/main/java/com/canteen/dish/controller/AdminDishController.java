package com.canteen.dish.controller;

import com.canteen.common.response.Result;
import com.canteen.dish.dto.DishResponse;
import com.canteen.dish.service.DishService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminDishController {
    private final DishService dishService;

    public AdminDishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/dishes")
    public Result<List<DishResponse>> getAllDishes() {
        return Result.success(dishService.getAll());
    }

    @PutMapping("/dish/{id}/force-off")
    public Result<Void> forceOff(@PathVariable Long id) {
        dishService.forceOff(id);
        return Result.success();
    }
}
