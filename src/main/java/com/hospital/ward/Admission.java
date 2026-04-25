package com.hospital.ward;

import com.hospital.common.BaseEntity;
import com.hospital.doctor.Doctor;
import com.hospital.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admissions", indexes = {
        @Index(name = "idx_admission_patient_id", columnList = "patient_id"),
        @Index(name = "idx_admission_bed_id", columnList = "bed_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id", nullable = false)
    private Bed bed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor admittingDoctor;

    @Column(nullable = false)
    private LocalDateTime admissionTime;

    @Column
    private LocalDateTime dischargeTime;

    @Column(columnDefinition = "TEXT")
    private String admissionReason;

    @Column(columnDefinition = "TEXT")
    private String dischargeNotes;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}
