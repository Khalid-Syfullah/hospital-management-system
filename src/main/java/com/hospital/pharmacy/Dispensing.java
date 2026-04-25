package com.hospital.pharmacy;

import com.hospital.common.BaseEntity;
import com.hospital.patient.Patient;
import com.hospital.prescription.Prescription;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dispensing_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dispensing extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Column(nullable = false)
    private int quantityDispensed;

    @Column(nullable = false)
    private LocalDateTime dispensedAt;

    @Column(length = 100)
    private String dispensedBy;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
