package com.hospital.lab;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LabMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java(order.getPatient().getFirstName() + ' ' + order.getPatient().getLastName())")
    @Mapping(target = "patientMrn", source = "patient.mrn")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", expression = "java(order.getDoctor().getUser().getFullName())")
    LabOrderResponse toResponse(LabOrder order);

    LabResultResponse toResultResponse(LabResult result);
}
