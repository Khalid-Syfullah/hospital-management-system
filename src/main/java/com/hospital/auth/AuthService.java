package com.hospital.auth;

import com.hospital.audit.AuditService;
import com.hospital.auth.AuthDtos.AuthResponse;
import com.hospital.auth.AuthDtos.LoginRequest;
import com.hospital.auth.AuthDtos.RefreshTokenRequest;
import com.hospital.auth.AuthDtos.RegisterRequest;
import com.hospital.security.JwtService;
import com.hospital.user.Role;
import com.hospital.user.User;
import com.hospital.user.UserRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditService auditService;
    private final int maxAttempts;
    private final long refreshTokenExpiration;

    public AuthService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuditService auditService,
            @Value("${application.security.login.max-attempts}") int maxAttempts,
            @Value("${application.security.jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.auditService = auditService;
        this.maxAttempts = maxAttempts;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Set<Role> roles = request.roles() == null || request.roles().isEmpty() ? Set.of(Role.PATIENT) : request.roles();
        User user = userRepository.save(new User(
                request.email().toLowerCase(),
                passwordEncoder.encode(request.password()),
                request.fullName(),
                roles));
        auditService.record("User", entityId(user.getId()), "REGISTER", "Account registered", user.getEmail());
        return issueTokens(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        if (!user.isEnabled() || user.isLocked()) {
            throw new BadCredentialsException("Account disabled or locked");
        }
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            user.markLoginFailure(maxAttempts);
            auditService.record("User", entityId(user.getId()), "LOGIN_FAILED", "Invalid password", user.getEmail());
            throw new BadCredentialsException("Invalid credentials");
        }
        user.markLoginSuccess();
        auditService.record("User", entityId(user.getId()), "LOGIN", "Successful login", user.getEmail());
        return issueTokens(user);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken current = refreshTokenRepository.findByTokenHash(hash(request.refreshToken()))
                .filter(RefreshToken::isActive)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
        current.revoke();
        return issueTokens(current.getUser());
    }

    private AuthResponse issueTokens(User user) {
        String refreshToken = UUID.randomUUID() + "." + UUID.randomUUID();
        refreshTokenRepository.save(new RefreshToken(
                hash(refreshToken),
                user,
                Instant.now().plusMillis(refreshTokenExpiration)));
        return new AuthResponse(user.getId(), jwtService.generateAccessToken(user), refreshToken, user.getRoles());
    }

    private String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 unavailable", ex);
        }
    }

    private String entityId(UUID id) {
        return id == null ? "pending" : id.toString();
    }
}
