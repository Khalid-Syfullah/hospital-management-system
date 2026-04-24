package com.hospital.user;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import com.hospital.user.UserDtos.UserResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    PageResponse<UserResponse> list(Pageable pageable) {
        return PageResponse.from("Users fetched", userService.list(pageable));
    }

    @PatchMapping("/{id}/enabled")
    ApiResponse<UserResponse> setEnabled(@PathVariable UUID id, @RequestParam boolean enabled) {
        return ApiResponse.success("User updated", userService.setEnabled(id, enabled));
    }
}
