package com.hospital.ward;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public final class WardDtos {
    private WardDtos() {
    }

    public record WardRequest(@NotBlank String name, String type) {
    }

    public record BedRequest(@NotNull UUID wardId, @NotBlank String bedNumber) {
    }

    public record AdmissionRequest(@NotNull UUID patientId) {
    }

    public record WardResponse(UUID id, String name, String type) {
    }

    public record BedResponse(UUID id, UUID wardId, String bedNumber, BedStatus status, UUID patientId) {
    }
}
