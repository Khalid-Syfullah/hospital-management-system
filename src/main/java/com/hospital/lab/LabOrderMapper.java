package com.hospital.lab;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LabOrderMapper { default LabOrderResponse toResponse(LabOrder order) { return LabOrderResponse.from(order); } }
