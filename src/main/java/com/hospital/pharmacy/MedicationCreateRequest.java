package com.hospital.pharmacy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MedicationCreateRequest {
    @NotBlank private String name;
    private String code;
    private String description;
    private String category;
    private String manufacturer;
    @NotNull private BigDecimal unitPrice;
    private Integer currentStock;
    private Integer reorderLevel;
    private LocalDate expiryDate;
    private boolean requiresPrescription;
    private String storageLocation;
}