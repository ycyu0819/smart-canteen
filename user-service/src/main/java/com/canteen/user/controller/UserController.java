package com.canteen.user.controller;

import com.canteen.common.auth.UserContext;
import com.canteen.common.response.Result;
import com.canteen.user.dto.UserProfileResponse;
import com.canteen.user.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping("/profile")
    public Result<UserProfileResponse> getProfile() {
        return Result.success(userService.getProfile(UserContext.getUserId()));
    }

    @PutMapping("/profile")
    public Result<UserProfileResponse> updateProfile(@RequestBody Map<String, String> body) {
        return Result.success(userService.updateProfile(
                UserContext.getUserId(), body.get("nickname"), body.get("avatarUrl")));
    }

    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody Map<String, String> body) {
        userService.changePassword(UserContext.getUserId(),
                body.get("oldPassword"), body.get("newPassword"));
        return Result.success();
    }

    @GetMapping("/verify/{userId}")
    public Result<Boolean> verifyUser(@PathVariable Long userId) {
        userService.getProfile(userId);
        return Result.success(true);
    }
}
