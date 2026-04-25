package com.hospital.user;

import java.util.Set;
import java.util.UUID;

public record UserResponse(UUID id, String email, String fullName, String phone, boolean enabled, Set<Role> roles) {
    static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getPhone(), user.isEnabled(), user.getRoles());
    }
}
