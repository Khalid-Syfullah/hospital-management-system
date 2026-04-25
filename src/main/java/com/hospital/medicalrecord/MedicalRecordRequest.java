package com.hospital.medicalrecord;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class MedicalRecordRequest {

    @NotNull
    private UUID patientId;

    @NotNull
    private UUID doctorId;

    private String diagnosis;

    private String icd10Code;

    private String symptoms;

    private String visitNotes;

    private String treatmentPlan;

    private String attachmentPaths;

    @Valid
    private VitalSigns vitalSigns;
}
