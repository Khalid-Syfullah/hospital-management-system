package com.hospital.lab;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LabOrderRequest {

    @NotNull
    private UUID patientId;

    @NotNull
    private UUID doctorId;

    @NotBlank
    private String testName;

    private String testCode;

    private String clinicalNotes;

    private String priority;
}
