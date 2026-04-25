package com.hospital.pharmacy;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MedicationResponse {
    private UUID id;
    private String name;
    private String code;
    private String description;
    private String category;
    private String manufacturer;
    private BigDecimal unitPrice;
    private Integer currentStock;
    private Integer reorderLevel;
    private LocalDate expiryDate;
    private boolean requiresPrescription;
    private String storageLocation;
    private boolean isActive;
    private LocalDateTime createdAt;

    public static MedicationResponse from(Medication m) {
        return new MedicationResponse(
                m.getId(), m.getName(), m.getCode(), m.getDescription(), m.getCategory(),
                m.getManufacturer(), m.getUnitPrice(), m.getCurrentStock(), m.getReorderLevel(),
                m.getExpiryDate(), m.isRequiresPrescription(), m.getStorageLocation(), m.isActive(), m.getCreatedAt()
        );
    }
}