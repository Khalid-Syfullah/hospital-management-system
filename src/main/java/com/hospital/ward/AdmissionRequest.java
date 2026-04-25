package com.hospital.ward;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AdmissionRequest {
    @NotNull private UUID patientId;
    @NotNull private UUID bedId;
    private UUID doctorId;
    private String admissionReason;
}
