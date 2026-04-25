package com.hospital.patient;

import com.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_vitals")
@Getter
@Setter
public class PatientVital extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private Double bloodPressureSystolic;
    private Double bloodPressureDiastolic;
    private Integer heartRate;
    private Double temperature;
    private Double weight;
    private Double height;
    private Double oxygenSaturation;
    private Integer respiratoryRate;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;
}