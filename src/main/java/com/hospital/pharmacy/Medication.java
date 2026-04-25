package com.hospital.pharmacy;

import com.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "medications")
@Getter
@Setter
public class Medication extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true)
    private String code;

    @Lob
    private String description;

    private String category;

    private String manufacturer;

    private BigDecimal unitPrice;

    @Column(name = "current_stock")
    private Integer currentStock = 0;

    @Column(name = "reorder_level")
    private Integer reorderLevel = 10;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "requires_prescription")
    private boolean requiresPrescription = false;

    @Column(name = "is_active")
    private boolean active = true;

    private String storageLocation;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}