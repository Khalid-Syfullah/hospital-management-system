package com.hospital.auth;

import com.hospital.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8) String password,
        @NotBlank String fullName,
        String phone,
        @NotEmpty Set<Role> roles) {}
