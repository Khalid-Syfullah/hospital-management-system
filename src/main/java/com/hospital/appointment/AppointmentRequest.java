package com.hospital.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AppointmentRequest(@NotNull UUID patientId, @NotNull UUID doctorId, @NotNull @Future OffsetDateTime startTime,
                                 @NotNull @Future OffsetDateTime endTime, String reason) {}
