package com.hospital.department;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    default DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.from(department);
    }
}
