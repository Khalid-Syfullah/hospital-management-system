package com.hospital.medicalrecord;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MedicalRecordResponse {
    private UUID id;
    private UUID patientId;
    private String patientName;
    private String patientMrn;
    private UUID doctorId;
    private String doctorName;
    private String diagnosis;
    private String icd10Code;
    private String symptoms;
    private String visitNotes;
    private String treatmentPlan;
    private String attachmentPaths;
    private VitalSigns vitalSigns;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
