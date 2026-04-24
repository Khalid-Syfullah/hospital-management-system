package com.hospital.appointment;

import com.hospital.appointment.AppointmentDtos.AppointmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "doctorId", source = "doctor.id")
    AppointmentResponse toResponse(Appointment appointment);
}
