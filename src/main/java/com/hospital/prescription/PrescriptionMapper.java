package com.hospital.prescription;

import com.hospital.prescription.PrescriptionDtos.PrescriptionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "doctorId", source = "doctor.id")
    PrescriptionResponse toResponse(Prescription prescription);
}
