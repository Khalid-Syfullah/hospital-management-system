package com.hospital.medicalrecord;

import com.hospital.medicalrecord.MedicalRecordDtos.MedicalRecordResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {
    @Mapping(target = "patientId", source = "patient.id")
    MedicalRecordResponse toResponse(MedicalRecord record);
}
