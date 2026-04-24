package com.hospital.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public final class UserDtos {
    private UserDtos() {
    }

    public record UserResponse(UUID id, String email, String fullName, Set<Role> roles, boolean enabled, Instant createdAt) {
    }

    public record UpdateProfileRequest(@NotBlank String fullName) {
    }

    public record CreateUserRequest(@Email @NotBlank String email, @NotBlank String password, @NotBlank String fullName, Set<Role> roles) {
    }
}
