package com.hospital.department;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public final class DepartmentDtos {
    private DepartmentDtos() {
    }

    public record DepartmentRequest(@NotBlank String name, String description, String headOfDepartment) {
    }

    public record DepartmentResponse(UUID id, String name, String description, String headOfDepartment) {
    }
}
