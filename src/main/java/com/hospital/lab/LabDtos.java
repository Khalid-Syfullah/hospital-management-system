package com.hospital.lab;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public final class LabDtos {
    private LabDtos() {
    }

    public record LabOrderRequest(@NotNull UUID patientId, @NotBlank String testName) {
    }

    public record LabResultRequest(@NotBlank String result) {
    }

    public record LabOrderResponse(UUID id, UUID patientId, String testName, LabOrderStatus status, String result) {
    }
}
