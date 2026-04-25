package com.hospital.doctor;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    default DoctorResponse toResponse(Doctor doctor) { return DoctorResponse.from(doctor); }
}
