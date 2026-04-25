package com.hospital.appointment;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    default AppointmentResponse toResponse(Appointment appointment) { return AppointmentResponse.from(appointment); }
}
