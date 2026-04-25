package com.hospital.prescription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PrescriptionRequest(@NotNull UUID patientId, @NotNull UUID doctorId, @NotBlank String medicineName,
                                  @NotBlank String dosage, @NotBlank String frequency, @NotBlank String duration,
                                  String instructions) {}
