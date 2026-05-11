package com.canteen.user.controller;

import com.canteen.common.auth.UserContext;
import com.canteen.common.response.Result;
import com.canteen.user.dto.LoginRequest;
import com.canteen.user.dto.LoginResponse;
import com.canteen.user.dto.RegisterRequest;
import com.canteen.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register/phone")
    public Result<Long> registerByPhone(@Valid @RequestBody RegisterRequest req) {
        return Result.success(authService.registerByPhone(req));
    }

    @PostMapping("/register/id")
    public Result<Long> registerByStudentId(@Valid @RequestBody RegisterRequest req) {
        return Result.success(authService.registerByStudentId(req));
    }

    @PostMapping("/login/password")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return Result.success(authService.login(req));
    }

    @PostMapping("/token/refresh")
    public Result<LoginResponse> refreshToken(@RequestParam String refreshToken) {
        return Result.success(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout(UserContext.getUserId());
        return Result.success();
    }
}
