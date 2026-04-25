package com.hospital.medicalrecord;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MedicalRecordRequest(@NotNull UUID patientId, @NotNull UUID doctorId, String icd10Code, String diagnoses,
                                   String symptoms, String visitNotes, String attachmentPath) {}
