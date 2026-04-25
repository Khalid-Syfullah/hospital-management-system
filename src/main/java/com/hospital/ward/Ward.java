package com.hospital.ward;

import com.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wards")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Ward extends BaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String wardName;

    @Column(nullable = false)
    private int totalBeds;

    @Column(nullable = false)
    private int occupiedBeds;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean active = true;
}
