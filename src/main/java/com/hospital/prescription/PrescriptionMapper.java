package com.hospital.prescription;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java(prescription.getPatient().getFirstName() + ' ' + prescription.getPatient().getLastName())")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", expression = "java(prescription.getDoctor().getUser().getFullName())")
    PrescriptionResponse toResponse(Prescription prescription);

    PrescriptionItemResponse toItemResponse(PrescriptionItem item);
}
