package com.hospital.department;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record DepartmentRequest(@NotBlank String name, String description, UUID headDoctorId) {}
