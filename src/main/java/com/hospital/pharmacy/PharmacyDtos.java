package com.hospital.pharmacy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

public final class PharmacyDtos {
    private PharmacyDtos() {
    }

    public record MedicationRequest(@NotBlank String name, String form, @Min(0) int stockQuantity,
            @Min(0) int lowStockThreshold, LocalDate expiryDate) {
    }

    public record DispenseRequest(@Min(1) int quantity) {
    }

    public record MedicationResponse(UUID id, String name, String form, int stockQuantity, int lowStockThreshold,
            LocalDate expiryDate, long version) {
    }
}
