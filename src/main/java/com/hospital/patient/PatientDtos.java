package com.hospital.patient;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

public final class PatientDtos {
    private PatientDtos() {
    }

    public record PatientRequest(
            @NotBlank String fullName,
            LocalDate dateOfBirth,
            String phone,
            String emergencyContact,
            String insuranceProvider,
            String insurancePolicyNumber,
            String medicalHistory,
            String allergies,
            String chronicConditions) {
    }

    public record PatientResponse(
            UUID id,
            String mrn,
            String fullName,
            LocalDate dateOfBirth,
            String phone,
            String emergencyContact,
            String insuranceProvider,
            String insurancePolicyNumber,
            String medicalHistory,
            String allergies,
            String chronicConditions) {
    }
}
