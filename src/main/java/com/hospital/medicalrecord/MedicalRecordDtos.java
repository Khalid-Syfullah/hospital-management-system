package com.hospital.medicalrecord;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public final class MedicalRecordDtos {
    private MedicalRecordDtos() {
    }

    public record MedicalRecordRequest(@NotNull UUID patientId, String icd10Code, String symptoms, String visitNotes, String vitals) {
    }

    public record MedicalRecordResponse(UUID id, UUID patientId, String icd10Code, String symptoms, String visitNotes, String vitals) {
    }
}
