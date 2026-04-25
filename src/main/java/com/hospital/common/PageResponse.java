package com.hospital.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
    private boolean success;
    private String message;
    private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public static <T> PageResponse<T> of(int page, int size, long totalElements, List<T> data) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageResponse<>(true, "Success", data, page, size, totalElements, totalPages);
    }
}