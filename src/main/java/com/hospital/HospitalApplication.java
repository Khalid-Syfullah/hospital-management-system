package com.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@EnableRetry
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
public class HospitalApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }
}
