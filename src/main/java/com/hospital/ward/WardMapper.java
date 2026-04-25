package com.hospital.ward;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WardMapper {

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "availableBeds", ignore = true)
    @Mapping(target = "occupiedBeds", ignore = true)
    WardResponse toResponse(Ward ward);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java(admission.getPatient().getFirstName() + ' ' + admission.getPatient().getLastName())")
    @Mapping(target = "patientMrn", source = "patient.mrn")
    @Mapping(target = "bedId", source = "bed.id")
    @Mapping(target = "bedNumber", source = "bed.bedNumber")
    @Mapping(target = "wardName", source = "bed.ward.name")
    @Mapping(target = "doctorId", source = "admittingDoctor.id")
    @Mapping(target = "doctorName", expression = "java(admission.getAdmittingDoctor() != null ? admission.getAdmittingDoctor().getUser().getFullName() : null)")
    AdmissionResponse toAdmissionResponse(Admission admission);
}
