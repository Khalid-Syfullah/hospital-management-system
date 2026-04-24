package com.hospital.patient;

import com.hospital.patient.PatientDtos.PatientResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientResponse toResponse(Patient patient);
}
