package com.hospital.doctor;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public final class DoctorDtos {
    private DoctorDtos() {
    }

    public record DoctorRequest(
            @NotBlank String fullName,
            @NotBlank String licenseNumber,
            String specialization,
            String availability,
            UUID departmentId) {
    }

    public record DoctorResponse(
            UUID id, String fullName, String licenseNumber, String specialization, String availability, UUID departmentId) {
    }
}
