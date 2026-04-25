package com.hospital.user;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    PageResponse<UserResponse> list(Pageable pageable) {
        return PageResponse.of("Users retrieved", service.list(pageable));
    }

    @GetMapping("/{id}")
    ApiResponse<UserResponse> get(@PathVariable UUID id) {
        return ApiResponse.ok("User retrieved", service.get(id));
    }

    @PutMapping("/{id}")
    ApiResponse<UserResponse> update(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.ok("User updated", service.update(id, request));
    }

    @PatchMapping("/{id}/enabled")
    ApiResponse<UserResponse> setEnabled(@PathVariable UUID id, @RequestParam boolean enabled) {
        return ApiResponse.ok("User status updated", service.setEnabled(id, enabled));
    }
}
