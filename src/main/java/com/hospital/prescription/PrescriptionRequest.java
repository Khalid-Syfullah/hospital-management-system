package com.hospital.prescription;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class PrescriptionRequest {

    @NotNull
    private UUID patientId;

    @NotNull
    private UUID doctorId;

    @NotNull
    private LocalDate prescriptionDate;

    private LocalDate expiryDate;

    private String notes;

    @NotEmpty
    @Valid
    private List<PrescriptionItemRequest> items;
}
