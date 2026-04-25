package com.hospital.lab;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record LabOrderRequest(@NotNull UUID patientId, @NotBlank String testName, LabOrder.Status status, String result) {}
