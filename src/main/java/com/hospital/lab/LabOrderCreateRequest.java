package com.hospital.lab;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LabOrderCreateRequest {
    @NotNull private UUID patientId;
    @NotNull private UUID doctorId;
    @NotBlank private String testName;
    private String testCode;
    private String description;
}