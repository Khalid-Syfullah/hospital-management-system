package com.hospital.department;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    Department toEntity(DepartmentRequest request);

    DepartmentResponse toResponse(Department department);

    void updateEntity(DepartmentRequest request, @MappingTarget Department department);
}
