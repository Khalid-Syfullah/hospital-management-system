package com.hospital.security;

import com.hospital.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    private final JwtProperties properties;
    private final SecretKey key;

    public JwtUtils(JwtProperties properties) {
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String accessToken(String subject, Collection<? extends GrantedAuthority> authorities) {
        Instant now = Instant.now();
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder().id(UUID.randomUUID().toString()).subject(subject).claim("roles", roles).issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(properties.accessTokenMinutes() * 60))).signWith(key).compact();
    }

    public String refreshToken(String subject) {
        Instant now = Instant.now();
        return Jwts.builder().id(UUID.randomUUID().toString()).subject(subject).claim("type", "refresh").issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(properties.refreshTokenDays() * 86_400))).signWith(key).compact();
    }

    public Claims parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public String subject(String token) {
        return parse(token).getSubject();
    }
}
