package com.hospital.patient;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "mrn", ignore = true)
    @Mapping(target = "allergies", ignore = true)
    Patient toEntity(PatientRequest request);

    PatientResponse toResponse(Patient patient);

    @Mapping(target = "mrn", ignore = true)
    @Mapping(target = "allergies", ignore = true)
    void updateEntity(PatientRequest request, @MappingTarget Patient patient);
}
