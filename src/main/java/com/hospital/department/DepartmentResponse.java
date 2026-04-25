package com.hospital.department;

import java.util.UUID;

public record DepartmentResponse(UUID id, String name, String description, UUID headDoctorId) {
    static DepartmentResponse from(Department department) {
        return new DepartmentResponse(department.getId(), department.getName(), department.getDescription(),
                department.getHeadDoctor() == null ? null : department.getHeadDoctor().getId());
    }
}
