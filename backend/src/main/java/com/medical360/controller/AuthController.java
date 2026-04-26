package com.medical360.controller;

import com.medical360.common.Result;
import com.medical360.entity.User;
import com.medical360.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return Result.success(token);
    }

    @GetMapping("/current")
    public Result<User> getCurrentUser(Authentication authentication) {
        User user = authService.getCurrentUser(authentication.getName());
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
