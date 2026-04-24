package com.hospital.pharmacy;

import com.hospital.pharmacy.PharmacyDtos.MedicationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicationMapper {
    MedicationResponse toResponse(Medication medication);
}
