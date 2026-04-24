package com.hospital.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hospital.audit.AuditService;
import com.hospital.auth.AuthDtos.RegisterRequest;
import com.hospital.security.JwtService;
import com.hospital.user.Role;
import com.hospital.user.UserRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Mock
    AuditService auditService;

    @Test
    void registerCreatesPatientByDefaultAndReturnsTokens() {
        when(userRepository.existsByEmailIgnoreCase("patient@example.com")).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(refreshTokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        AuthService service = new AuthService(
                userRepository,
                refreshTokenRepository,
                new BCryptPasswordEncoder(),
                new JwtService("QWJjZGVmR2hpamtsTW5vcFFyc3R1dnd4eXoxMjM0NTY3ODkwMTIzNDU2Nzg5", 900000),
                auditService,
                5,
                604800000);

        var response = service.register(new RegisterRequest("patient@example.com", "password123", "Patient One", Set.of()));

        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
        assertThat(response.roles()).containsExactly(Role.PATIENT);
    }

    @Test
    void loginRejectsUnknownUser() {
        when(userRepository.findByEmailIgnoreCase("missing@example.com")).thenReturn(Optional.empty());
        AuthService service = new AuthService(
                userRepository,
                refreshTokenRepository,
                new BCryptPasswordEncoder(),
                new JwtService("QWJjZGVmR2hpamtsTW5vcFFyc3R1dnd4eXoxMjM0NTY3ODkwMTIzNDU2Nzg5", 900000),
                auditService,
                5,
                604800000);

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.security.authentication.BadCredentialsException.class,
                () -> service.login(new AuthDtos.LoginRequest("missing@example.com", "password123")));
    }
}
