package com.hospital.prescription;

import com.hospital.common.BaseEntity;
import com.hospital.doctor.Doctor;
import com.hospital.medicalrecord.MedicalRecord;
import com.hospital.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prescriptions")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Prescription extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    @Column(nullable = false, length = 100)
    private String medicineName;

    @Column(nullable = false, length = 100)
    private String dosage;

    @Column(nullable = false, length = 100)
    private String frequency;

    @Column(nullable = false)
    private int durationDays;

    @Column(length = 500)
    private String instructions;

    @Column(length = 500)
    private String sideEffects;
}
