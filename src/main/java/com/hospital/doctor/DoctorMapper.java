package com.hospital.doctor;

import com.hospital.doctor.DoctorDtos.DoctorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(target = "departmentId", source = "department.id")
    DoctorResponse toResponse(Doctor doctor);
}
