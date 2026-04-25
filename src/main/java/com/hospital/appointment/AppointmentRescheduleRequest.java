package com.hospital.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRescheduleRequest {
    @NotNull(message = "New appointment date is required")
    private LocalDateTime newAppointmentDate;
    private String reason;
}