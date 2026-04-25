package com.hospital.appointment;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AppointmentResponse(UUID id, UUID patientId, UUID doctorId, OffsetDateTime startTime,
                                  OffsetDateTime endTime, Appointment.Status status, String reason) {
    static AppointmentResponse from(Appointment a) {
        return new AppointmentResponse(a.getId(), a.getPatient().getId(), a.getDoctor().getId(), a.getStartTime(), a.getEndTime(), a.getStatus(), a.getReason());
    }
}
