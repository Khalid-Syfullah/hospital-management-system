package com.hospital.pharmacy;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicationMapper { default MedicationResponse toResponse(Medication medication) { return MedicationResponse.from(medication); } }
