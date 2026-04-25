package com.hospital.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    private UUID patientId;
    private UUID doctorId;
    private LocalDateTime appointmentDateTime;
    private int durationMinutes;
    private String reason;
    private String notes;
    private String idempotencyKey;
}
