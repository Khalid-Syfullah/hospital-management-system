package com.hospital.lab;

import com.hospital.lab.LabDtos.LabOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LabOrderMapper {
    @Mapping(target = "patientId", source = "patient.id")
    LabOrderResponse toResponse(LabOrder order);
}
