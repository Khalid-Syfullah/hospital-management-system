package com.hospital.auth;

import com.hospital.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.ok("Registered", service.register(request));
    }

    @PostMapping("/login")
    ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok("Authenticated", service.login(request));
    }

    @PostMapping("/refresh")
    ApiResponse<AuthResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        return ApiResponse.ok("Token refreshed", service.refresh(request));
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@Valid @RequestBody TokenRefreshRequest request) {
        service.logout(request);
        return ApiResponse.ok("Logged out", null);
    }
}
