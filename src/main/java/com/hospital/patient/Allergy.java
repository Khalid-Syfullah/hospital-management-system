package com.hospital.patient;

import com.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient_allergies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Allergy extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false, length = 100)
    private String allergen;

    @Column(length = 50)
    private String severity;

    @Column(columnDefinition = "TEXT")
    private String reaction;
}
