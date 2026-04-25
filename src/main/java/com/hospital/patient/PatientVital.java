package com.hospital.patient;

import com.hospital.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "patient_vitals")
public class PatientVital extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    private String bloodPressure;
    private Integer heartRate;
    private BigDecimal temperatureCelsius;
    private BigDecimal weightKg;
    private BigDecimal heightCm;
    private Instant measuredAt = Instant.now();
}
