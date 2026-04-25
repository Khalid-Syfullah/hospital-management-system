package com.hospital.pharmacy;

import com.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "medications")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Pharmacy extends BaseEntity {
    @Column(nullable = false, unique = true, length = 100)
    private String medicineName;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private int reorderLevel;

    @Column(nullable = false, length = 50)
    private String unit;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false, length = 50)
    private String batch;

    @Column(length = 500)
    private String description;
}
