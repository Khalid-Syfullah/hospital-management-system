package com.hospital.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RescheduleRequest {

    @NotNull
    @Future
    private LocalDateTime newStartTime;

    @NotNull
    @Future
    private LocalDateTime newEndTime;
}
