package com.hospital.department;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentResponse toResponse(Department department);

    Department toEntity(DepartmentRequest request);
}
