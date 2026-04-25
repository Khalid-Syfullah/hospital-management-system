package com.hospital.appointment;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentResponse {
    private UUID id;
    private UUID patientId;
    private String patientName;
    private String patientMrn;
    private UUID doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AppointmentStatus status;
    private String reason;
    private String notes;
    private String idempotencyKey;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
