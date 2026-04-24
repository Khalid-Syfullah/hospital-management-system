package com.hospital.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final long authCapacity;
    private final long authRefillMinutes;

    public RateLimitingFilter(
            @Value("${application.rate-limit.auth-capacity}") long authCapacity,
            @Value("${application.rate-limit.auth-refill-minutes}") long authRefillMinutes) {
        this.authCapacity = authCapacity;
        this.authRefillMinutes = authRefillMinutes;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/v1/auth/")) {
            String key = request.getRemoteAddr() + ":" + request.getRequestURI();
            Bucket bucket = buckets.computeIfAbsent(key, ignored -> newBucket());
            if (!bucket.tryConsume(1)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"success\":false,\"message\":\"Rate limit exceeded\",\"errors\":[\"Too many requests\"]}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private Bucket newBucket() {
        Refill refill = Refill.greedy(authCapacity, Duration.ofMinutes(authRefillMinutes));
        Bandwidth limit = Bandwidth.classic(authCapacity, refill);
        return Bucket.builder().addLimit(limit).build();
    }
}
