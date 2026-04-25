package com.hospital.common;

import java.util.List;

public record ApiResponse<T>(boolean success, String message, T data, List<String> errors) {
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, List.of());
    }

    public static ApiResponse<Void> error(String message, List<String> errors) {
        return new ApiResponse<>(false, message, null, errors);
    }
}
