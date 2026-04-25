package com.hospital.patient;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    default PatientResponse toResponse(Patient patient) { return PatientResponse.from(patient); }
}
