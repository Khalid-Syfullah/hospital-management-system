package com.hospital.common;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<T>(
        boolean success,
        String message,
        List<T> data,
        int page,
        int size,
        long totalElements,
        int totalPages) {
    public static <T> PageResponse<T> of(String message, Page<T> page) {
        return new PageResponse<>(true, message, page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
