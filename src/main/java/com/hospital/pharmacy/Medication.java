package com.hospital.pharmacy;

import com.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "medications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 100)
    private String genericName;

    @Column(length = 100)
    private String brand;

    @Column(length = 100)
    private String dosageForm;

    @Column(length = 50)
    private String strength;

    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    @Builder.Default
    private int stockQuantity = 0;

    @Column(nullable = false)
    @Builder.Default
    private int reorderLevel = 10;

    @Column
    private LocalDate expiryDate;

    @Column(length = 50)
    private String batchNumber;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Version
    private Long version;
}
