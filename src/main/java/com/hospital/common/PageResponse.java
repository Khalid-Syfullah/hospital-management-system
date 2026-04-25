package com.hospital.common;

import org.springframework.data.domain.Page;
import java.util.List;

public class PageResponse<T> {
    private boolean success;
    private String message;
    private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PageResponse() {}

    public PageResponse(boolean success, String message, List<T> data, int page, int size, long totalElements, int totalPages) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public static <T> PageResponseBuilder<T> builder() { return new PageResponseBuilder<>(); }

    public static class PageResponseBuilder<T> {
        private boolean success;
        private String message;
        private List<T> data;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        public PageResponseBuilder<T> success(boolean success) { this.success = success; return this; }
        public PageResponseBuilder<T> message(String message) { this.message = message; return this; }
        public PageResponseBuilder<T> data(List<T> data) { this.data = data; return this; }
        public PageResponseBuilder<T> page(int page) { this.page = page; return this; }
        public PageResponseBuilder<T> size(int size) { this.size = size; return this; }
        public PageResponseBuilder<T> totalElements(long totalElements) { this.totalElements = totalElements; return this; }
        public PageResponseBuilder<T> totalPages(int totalPages) { this.totalPages = totalPages; return this; }
        public PageResponse<T> build() { return new PageResponse<>(success, message, data, page, size, totalElements, totalPages); }
    }

    public static <T> PageResponse<T> of(Page<T> page, String message) {
        return PageResponse.<T>builder()
                .success(true)
                .message(message)
                .data(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<T> getData() { return data; }
    public void setData(List<T> data) { this.data = data; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
}
