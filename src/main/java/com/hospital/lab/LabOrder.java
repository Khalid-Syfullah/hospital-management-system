package com.hospital.lab;

import com.hospital.common.BaseEntity;
import com.hospital.doctor.Doctor;
import com.hospital.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lab_orders", indexes = {
        @Index(name = "idx_lab_patient_id", columnList = "patient_id"),
        @Index(name = "idx_lab_doctor_id", columnList = "doctor_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabOrder extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false, length = 200)
    private String testName;

    @Column(length = 100)
    private String testCode;

    @Column(columnDefinition = "TEXT")
    private String clinicalNotes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private LabStatus status = LabStatus.REQUESTED;

    @Column(length = 100)
    private String priority;

    @OneToOne(mappedBy = "labOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private LabResult result;
}
