package com.hospital.prescription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public final class PrescriptionDtos {
    private PrescriptionDtos() {
    }

    public record PrescriptionRequest(@NotNull UUID patientId, @NotNull UUID doctorId, @NotBlank String medicineName,
            String dosage, String frequency, String duration, String instructions) {
    }

    public record PrescriptionResponse(UUID id, UUID patientId, UUID doctorId, String medicineName, String dosage,
            String frequency, String duration, String instructions) {
    }
}
