package com.hospital.prescription;

import com.hospital.common.BaseEntity;
import com.hospital.doctor.Doctor;
import com.hospital.medicalrecord.MedicalRecord;
import com.hospital.patient.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
public class Prescription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    @Column(name = "prescription_date", nullable = false)
    private LocalDateTime prescriptionDate;

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    @Column(name = "dosage", nullable = false)
    private String dosage;

    @Column(name = "frequency", nullable = false)
    private String frequency;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Lob
    @Column(name = "instructions")
    private String instructions;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrescriptionStatus status = PrescriptionStatus.ACTIVE;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public enum PrescriptionStatus {
        ACTIVE, COMPLETED, CANCELLED
    }
}