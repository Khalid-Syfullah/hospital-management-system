package com.hospital.pharmacy;

import java.time.LocalDate;
import java.util.UUID;

public record MedicationResponse(UUID id, String name, String strength, String form, int stockQuantity,
                                 int lowStockThreshold, LocalDate expiryDate, boolean lowStock) {
    static MedicationResponse from(Medication m) { return new MedicationResponse(m.getId(), m.getName(), m.getStrength(), m.getForm(), m.getStockQuantity(), m.getLowStockThreshold(), m.getExpiryDate(), m.getStockQuantity() <= m.getLowStockThreshold()); }
}
