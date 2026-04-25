package com.hospital.doctor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    DoctorResponse toResponse(Doctor doctor);

    @Mapping(target = "department", ignore = true)
    Doctor toEntity(DoctorRequest request);
}
