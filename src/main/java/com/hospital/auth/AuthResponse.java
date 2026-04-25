package com.hospital.auth;

import com.hospital.user.Role;
import java.util.Set;
import java.util.UUID;

public record AuthResponse(UUID userId, String email, Set<Role> roles, String accessToken, String refreshToken) {}
