package com.hospital.auth;

import com.hospital.audit.AuditService;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.UnauthorizedException;
import com.hospital.security.JwtProperties;
import com.hospital.security.JwtUtils;
import com.hospital.user.Role;
import com.hospital.user.User;
import com.hospital.user.UserRepository;
import java.time.Instant;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private final UserRepository users;
    private final RefreshTokenRepository refreshTokens;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;
    private final AuditService auditService;

    public AuthService(UserRepository users, RefreshTokenRepository refreshTokens, PasswordEncoder encoder,
                       AuthenticationManager authenticationManager, JwtUtils jwtUtils, JwtProperties jwtProperties,
                       AuditService auditService) {
        this.users = users;
        this.refreshTokens = refreshTokens;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.jwtProperties = jwtProperties;
        this.auditService = auditService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (users.existsByEmailIgnoreCase(request.email())) {
            throw new BadRequestException("Email is already registered");
        }
        User user = new User();
        user.setEmail(request.email().toLowerCase());
        user.setPasswordHash(encoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setPhone(request.phone());
        user.setRoles(Set.copyOf(request.roles()));
        users.save(user);
        auditService.record("User", user.getId().toString(), "AUTH_REGISTER", "registered");
        return tokensFor(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = users.findByEmailIgnoreCase(request.email()).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
            user.setFailedLoginAttempts(0);
            user.setAccountLocked(false);
            user.setLockedUntil(null);
            auditService.record("User", user.getId().toString(), "AUTH_LOGIN", "success");
            return tokensFor(user);
        } catch (BadCredentialsException ex) {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);
            if (attempts >= MAX_FAILED_ATTEMPTS) {
                user.setAccountLocked(true);
                user.setLockedUntil(Instant.now().plusSeconds(900));
            }
            auditService.record("User", user.getId().toString(), "AUTH_LOGIN_FAILED", "failed");
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    @Transactional
    public AuthResponse refresh(TokenRefreshRequest request) {
        RefreshToken current = refreshTokens.findByTokenAndRevokedFalse(request.refreshToken())
                .filter(token -> token.getExpiresAt().isAfter(Instant.now()))
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
        current.setRevoked(true);
        auditService.record("User", current.getUser().getId().toString(), "AUTH_REFRESH", "rotated refresh token");
        return tokensFor(current.getUser());
    }

    @Transactional
    public void logout(TokenRefreshRequest request) {
        refreshTokens.findByTokenAndRevokedFalse(request.refreshToken()).ifPresent(token -> token.setRevoked(true));
    }

    private AuthResponse tokensFor(User user) {
        var authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).toList();
        String access = jwtUtils.accessToken(user.getEmail(), authorities);
        String refresh = jwtUtils.refreshToken(user.getEmail());
        RefreshToken entity = new RefreshToken();
        entity.setToken(refresh);
        entity.setUser(user);
        entity.setExpiresAt(Instant.now().plusSeconds(jwtProperties.refreshTokenDays() * 86_400));
        refreshTokens.save(entity);
        return new AuthResponse(user.getId(), user.getEmail(), user.getRoles(), access, refresh);
    }
}
