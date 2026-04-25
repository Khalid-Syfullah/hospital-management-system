package com.hospital.prescription;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper { default PrescriptionResponse toResponse(Prescription p) { return PrescriptionResponse.from(p); } }
