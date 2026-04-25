package com.hospital.pharmacy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DispenseRequest {

    @NotNull
    private UUID patientId;

    private UUID prescriptionId;

    @NotNull
    private UUID medicationId;

    @NotNull
    @Min(1)
    private int quantity;

    private String notes;
}
