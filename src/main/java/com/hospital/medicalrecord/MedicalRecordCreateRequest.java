package com.hospital.medicalrecord;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MedicalRecordCreateRequest {
    @NotNull(message = "Patient ID is required")
    private UUID patientId;
    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;
    private UUID appointmentId;
    private LocalDateTime visitDate;
    private String chiefComplaint;
    private String symptoms;
    private String diagnosis;
    private String icd10Code;
    private String treatmentPlan;
    private String followUpInstructions;
    private String bloodPressure;
    private Integer heartRate;
    private Double temperature;
    private Double weight;
    private Double height;
    private Double oxygenSaturation;
    private String attachmentPath;
}