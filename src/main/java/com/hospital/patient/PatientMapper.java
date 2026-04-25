package com.hospital.patient;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientResponse toResponse(Patient patient);

    Patient toEntity(PatientRequest request);
}
