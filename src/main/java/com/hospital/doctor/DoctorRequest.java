package com.hospital.doctor;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

public record DoctorRequest(@NotBlank String fullName, @NotBlank String licenseNumber, String credentials,
                            Set<String> specializations, String availability, UUID departmentId, UUID userId) {}
