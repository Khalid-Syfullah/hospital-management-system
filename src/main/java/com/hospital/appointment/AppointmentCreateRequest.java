package com.hospital.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentCreateRequest {
    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotNull(message = "Appointment date is required")
    private LocalDateTime appointmentDate;

    @NotBlank(message = "Reason for visit is required")
    private String reasonForVisit;

    private Integer slotDurationMinutes;
    private String idempotencyKey;
}