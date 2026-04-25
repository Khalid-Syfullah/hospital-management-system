package com.hospital.prescription;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PrescriptionResponse {
    private UUID id;
    private UUID patientId;
    private String patientName;
    private UUID doctorId;
    private String doctorName;
    private LocalDate prescriptionDate;
    private LocalDate expiryDate;
    private String notes;
    private PrescriptionStatus status;
    private List<PrescriptionItemResponse> items;
    private LocalDateTime createdAt;
}

@Data
class PrescriptionItemResponse {
    private UUID id;
    private String medicineName;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;
    private Integer quantity;
}
