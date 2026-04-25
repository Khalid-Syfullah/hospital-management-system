package com.hospital.auth;

import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.TokenRefreshException;
import com.hospital.security.JwtUtils;
import com.hospital.user.Role;
import com.hospital.user.User;
import com.hospital.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private JwtUtils jwtUtils;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "refreshExpirationMs", 604800000L);
    }

    @Test
    @DisplayName("Register succeeds for new email")
    void register_success() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(Set.of(Role.PATIENT))
                .build();
        ReflectionTestUtils.setField(savedUser, "id", UUID.randomUUID());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtils.generateToken(any(User.class))).thenReturn("access-token");

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(3600))
                .user(savedUser)
                .build();
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);

        AuthResponse response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Register throws when email already exists")
    void register_duplicateEmail_throws() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        request.setPassword("password123");
        request.setFirstName("Jane");
        request.setLastName("Doe");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("existing@example.com");
    }

    @Test
    @DisplayName("Refresh token succeeds with valid token")
    void refreshToken_success() {
        User user = User.builder()
                .email("user@example.com")
                .roles(Set.of(Role.PATIENT))
                .build();
        ReflectionTestUtils.setField(user, "id", UUID.randomUUID());

        RefreshToken token = RefreshToken.builder()
                .token("valid-token")
                .user(user)
                .expiryDate(Instant.now().plusSeconds(3600))
                .revoked(false)
                .build();

        when(refreshTokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));
        when(jwtUtils.generateToken(any(User.class))).thenReturn("new-access-token");
        when(refreshTokenRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AuthResponse response = authService.refreshToken("valid-token");

        assertThat(response.getAccessToken()).isEqualTo("new-access-token");
        assertThat(token.isRevoked()).isTrue();
    }

    @Test
    @DisplayName("Refresh token fails when token is revoked")
    void refreshToken_revoked_throws() {
        RefreshToken token = RefreshToken.builder()
                .token("revoked-token")
                .expiryDate(Instant.now().plusSeconds(3600))
                .revoked(true)
                .build();

        when(refreshTokenRepository.findByToken("revoked-token")).thenReturn(Optional.of(token));

        assertThatThrownBy(() -> authService.refreshToken("revoked-token"))
                .isInstanceOf(TokenRefreshException.class);
    }

    @Test
    @DisplayName("Refresh token fails when token is expired")
    void refreshToken_expired_throws() {
        RefreshToken token = RefreshToken.builder()
                .token("expired-token")
                .expiryDate(Instant.now().minusSeconds(3600))
                .revoked(false)
                .build();

        when(refreshTokenRepository.findByToken("expired-token")).thenReturn(Optional.of(token));

        assertThatThrownBy(() -> authService.refreshToken("expired-token"))
                .isInstanceOf(TokenRefreshException.class);
    }

    @Test
    @DisplayName("Login authenticates and returns tokens")
    void login_success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("password");

        User user = User.builder()
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(Set.of(Role.PATIENT))
                .build();
        ReflectionTestUtils.setField(user, "id", UUID.randomUUID());

        when(userRepository.findByEmailAndDeletedAtIsNull("user@example.com")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(any(User.class))).thenReturn("access-token");
        when(userRepository.save(any())).thenReturn(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token("refresh-token")
                .user(user)
                .expiryDate(Instant.now().plusSeconds(3600))
                .build();
        when(refreshTokenRepository.save(any())).thenReturn(refreshToken);

        AuthResponse response = authService.login(request);

        assertThat(response.getEmail()).isEqualTo("user@example.com");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
