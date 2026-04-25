package com.hospital.pharmacy;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MedicationResponse {
    private UUID id;
    private String name;
    private String genericName;
    private String brand;
    private String dosageForm;
    private String strength;
    private BigDecimal unitPrice;
    private int stockQuantity;
    private int reorderLevel;
    private boolean lowStock;
    private LocalDate expiryDate;
    private String batchNumber;
    private boolean active;
    private LocalDateTime createdAt;
}
