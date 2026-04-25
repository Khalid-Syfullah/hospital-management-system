package com.hospital.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponse {
    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
}
