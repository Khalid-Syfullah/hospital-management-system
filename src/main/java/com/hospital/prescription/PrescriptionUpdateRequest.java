package com.hospital.prescription;

import lombok.Data;

@Data
public class PrescriptionUpdateRequest {
    private String dosage;
    private String frequency;
    private String instructions;
    private String status;
}