package com.hospital.medicalrecord;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {
    default MedicalRecordResponse toResponse(MedicalRecord record) { return MedicalRecordResponse.from(record); }
}
