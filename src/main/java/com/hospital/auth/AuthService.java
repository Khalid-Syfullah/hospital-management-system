package com.hospital.auth;

import com.hospital.exception.BadRequestException;
import com.hospital.exception.UnauthorizedException;
import com.hospital.security.JwtUtils;
import com.hospital.user.Role;
import com.hospital.user.User;
import com.hospital.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone number already exists");
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + request.getRole());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRole(role);
        user.setEnabled(true);

        user = userRepository.save(user);
        log.info("New user registered: {} with role: {}", user.getEmail(), role);

        return generateAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!user.isEnabled()) {
            throw new UnauthorizedException("Account is disabled");
        }

        if (user.isAccountLocked()) {
            throw new UnauthorizedException("Account is locked until " + user.getLockedUntil());
        }

        user.resetFailedAttempts();
        user = userRepository.save(user);

        log.info("User logged in: {}", user.getEmail());
        return generateAuthResponse(user);
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtUtils.validateToken(refreshToken) || !jwtUtils.isRefreshToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        UUID userId = jwtUtils.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new UnauthorizedException("Refresh token has been revoked");
        }

        if (!user.isEnabled()) {
            throw new UnauthorizedException("Account is disabled");
        }

        log.info("Refreshing token for user: {}", user.getEmail());
        return generateAuthResponse(user);
    }

    @Transactional
    public void logout(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
        user.setRefreshToken(null);
        userRepository.save(user);
        log.info("User logged out: {}", user.getEmail());
    }

    private AuthResponse generateAuthResponse(User user) {
        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtils.generateRefreshToken(user.getId());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                900000,
                user.getEmail(),
                user.getRole().name()
        );
    }
}