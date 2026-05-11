package com.canteen.user.controller;

import com.canteen.common.response.Result;
import com.canteen.user.dto.UserProfileResponse;
import com.canteen.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) { this.userService = userService; }

    @GetMapping
    public Result<Page<UserProfileResponse>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        return Result.success(userService.listUsers(page, size, role, status));
    }

    @PutMapping("/{id}/toggle-status")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        userService.toggleUserStatus(id);
        return Result.success();
    }
}
