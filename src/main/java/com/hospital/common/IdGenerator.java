package com.hospital.common;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class IdGenerator {
    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis() % 100000);

    public String generateMRN() {
        return "MRN" + String.format("%06d", counter.incrementAndGet());
    }
}