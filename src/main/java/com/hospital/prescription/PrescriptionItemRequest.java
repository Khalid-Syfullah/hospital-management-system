package com.hospital.prescription;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrescriptionItemRequest {

    @NotBlank
    private String medicineName;

    private String dosage;

    private String frequency;

    private String duration;

    private String instructions;

    private Integer quantity;
}
