package com.hospital.medicalrecord;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java(record.getPatient().getFirstName() + ' ' + record.getPatient().getLastName())")
    @Mapping(target = "patientMrn", source = "patient.mrn")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", expression = "java(record.getDoctor().getUser().getFullName())")
    MedicalRecordResponse toResponse(MedicalRecord record);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "vitalHistory", ignore = true)
    void updateEntity(MedicalRecordRequest request, @MappingTarget MedicalRecord record);
}
