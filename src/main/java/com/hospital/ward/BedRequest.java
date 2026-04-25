package com.hospital.ward;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record BedRequest(@NotNull UUID wardId, @NotBlank String bedNumber, Bed.Status status, UUID patientId) {}
