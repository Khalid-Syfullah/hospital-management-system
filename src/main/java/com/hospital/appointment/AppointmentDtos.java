package com.hospital.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public final class AppointmentDtos {
    private AppointmentDtos() {
    }

    public record BookAppointmentRequest(
            @NotNull UUID patientId,
            @NotNull UUID doctorId,
            @Future @NotNull LocalDateTime startTime,
            @Future @NotNull LocalDateTime endTime) {
    }

    public record RescheduleAppointmentRequest(@Future @NotNull LocalDateTime startTime, @Future @NotNull LocalDateTime endTime) {
    }

    public record AppointmentResponse(
            UUID id,
            UUID patientId,
            UUID doctorId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            AppointmentStatus status,
            long version) {
    }
}
