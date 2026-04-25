package com.hospital.prescription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class PrescriptionCreateRequest {
    @NotNull private UUID patientId;
    @NotNull private UUID doctorId;
    private UUID medicalRecordId;
    @NotBlank private String medicineName;
    @NotBlank private String dosage;
    @NotBlank private String frequency;
    private Integer durationDays;
    private String instructions;
    private LocalDate startDate;
    private LocalDate endDate;
}