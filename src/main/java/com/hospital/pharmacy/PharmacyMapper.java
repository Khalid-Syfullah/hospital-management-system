package com.hospital.pharmacy;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PharmacyMapper {

    @Mapping(target = "lowStock", expression = "java(medication.getStockQuantity() <= medication.getReorderLevel())")
    MedicationResponse toResponse(Medication medication);

    @Mapping(target = "active", ignore = true)
    Medication toEntity(MedicationRequest request);

    @Mapping(target = "active", ignore = true)
    void updateEntity(MedicationRequest request, @MappingTarget Medication medication);
}
