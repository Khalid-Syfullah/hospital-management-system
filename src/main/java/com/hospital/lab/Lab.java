package com.hospital.lab;

import com.hospital.common.BaseEntity;
import com.hospital.doctor.Doctor;
import com.hospital.patient.Patient;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lab_tests")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Lab extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false, length = 100)
    private String testName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LabStatus status;

    @Column
    private LocalDateTime resultDate;

    @Column(length = 1000)
    private String result;

    @Column(length = 1000)
    private String notes;

    public enum LabStatus {
        REQUESTED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}
