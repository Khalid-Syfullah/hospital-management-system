package com.hospital.appointment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java(appointment.getPatient().getFirstName() + ' ' + appointment.getPatient().getLastName())")
    @Mapping(target = "patientMrn", source = "patient.mrn")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", expression = "java(appointment.getDoctor().getUser().getFullName())")
    @Mapping(target = "doctorSpecialization", source = "doctor.specialization")
    AppointmentResponse toResponse(Appointment appointment);
}
