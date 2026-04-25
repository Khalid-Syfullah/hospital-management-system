package com.hospital.pharmacy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record MedicationRequest(@NotBlank String name, String strength, String form, @Min(0) int stockQuantity,
                                @Min(0) int lowStockThreshold, LocalDate expiryDate) {}
