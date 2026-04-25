package com.hospital.doctor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "doctorName", expression = "java(doctor.getUser().getFullName())")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    DoctorResponse toResponse(Doctor doctor);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "availabilitySlots", ignore = true)
    void updateEntity(DoctorRequest request, @MappingTarget Doctor doctor);
}
