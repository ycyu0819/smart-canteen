package com.canteen.dish.controller;

import com.canteen.common.auth.UserContext;
import com.canteen.common.response.Result;
import com.canteen.dish.dto.CategoryRequest;
import com.canteen.dish.entity.Category;
import com.canteen.dish.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Result<Category> create(@RequestBody CategoryRequest req) {
        return Result.success(categoryService.create(UserContext.getUserId(), req));
    }

    @GetMapping("/list")
    public Result<List<Category>> getMyCategories() {
        return Result.success(categoryService.getByMerchant(UserContext.getUserId()));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody CategoryRequest req) {
        categoryService.update(id, UserContext.getUserId(), req);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id, UserContext.getUserId());
        return Result.success();
    }
}
