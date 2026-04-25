package com.hospital.pharmacy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MedicationRequest {

    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 100)
    private String genericName;

    @Size(max = 100)
    private String brand;

    @Size(max = 100)
    private String dosageForm;

    @Size(max = 50)
    private String strength;

    private BigDecimal unitPrice;

    @Min(0)
    private int stockQuantity;

    @Min(0)
    private int reorderLevel;

    private LocalDate expiryDate;

    @Size(max = 50)
    private String batchNumber;
}
