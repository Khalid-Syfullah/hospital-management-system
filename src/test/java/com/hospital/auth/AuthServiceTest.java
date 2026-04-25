package com.hospital.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hospital.audit.AuditService;
import com.hospital.security.JwtProperties;
import com.hospital.security.JwtUtils;
import com.hospital.user.Role;
import com.hospital.user.User;
import com.hospital.user.UserRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class AuthServiceTest {
    @Test
    void registerCreatesUserAndTokens() {
        UserRepository users = mock(UserRepository.class);
        RefreshTokenRepository refreshTokens = mock(RefreshTokenRepository.class);
        when(users.existsByEmailIgnoreCase("admin@example.com")).thenReturn(false);
        when(users.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(java.util.UUID.randomUUID());
            return user;
        });
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtProperties props = new JwtProperties("test-secret-test-secret-test-secret-test-secret-test-secret", 15, 7);
        AuthService service = new AuthService(users, refreshTokens, new BCryptPasswordEncoder(), authenticationManager,
                new JwtUtils(props), props, mock(AuditService.class));

        AuthResponse response = service.register(new RegisterRequest("admin@example.com", "StrongPass123", "Admin", null, Set.of(Role.ADMIN)));

        assertThat(response.email()).isEqualTo("admin@example.com");
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
    }

    @Test
    void refreshRotatesToken() {
        User user = new User();
        user.setId(java.util.UUID.randomUUID());
        user.setEmail("user@example.com");
        user.setRoles(Set.of(Role.PATIENT));
        JwtProperties props = new JwtProperties("test-secret-test-secret-test-secret-test-secret-test-secret", 15, 7);
        JwtUtils jwtUtils = new JwtUtils(props);
        RefreshToken token = new RefreshToken();
        token.setToken(jwtUtils.refreshToken("user@example.com"));
        token.setUser(user);
        token.setExpiresAt(java.time.Instant.now().plusSeconds(60));
        RefreshTokenRepository refreshTokens = mock(RefreshTokenRepository.class);
        when(refreshTokens.findByTokenAndRevokedFalse(token.getToken())).thenReturn(Optional.of(token));
        AuthService service = new AuthService(mock(UserRepository.class), refreshTokens, new BCryptPasswordEncoder(),
                mock(AuthenticationManager.class), jwtUtils, props, mock(AuditService.class));

        AuthResponse response = service.refresh(new TokenRefreshRequest(token.getToken()));

        assertThat(token.isRevoked()).isTrue();
        assertThat(response.refreshToken()).isNotEqualTo(token.getToken());
    }
}
