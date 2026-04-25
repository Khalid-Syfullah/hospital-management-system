package com.hospital.medicalrecord;

import lombok.Data;

@Data
public class MedicalRecordUpdateRequest {
    private String diagnosis;
    private String icd10Code;
    private String treatmentPlan;
    private String followUpInstructions;
}