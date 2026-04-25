package com.hospital.prescription;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PrescriptionResponse {
    private UUID id;
    private UUID patientId;
    private String patientName;
    private UUID doctorId;
    private String doctorName;
    private LocalDateTime prescriptionDate;
    private String medicineName;
    private String dosage;
    private String frequency;
    private Integer durationDays;
    private String instructions;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;

    public static PrescriptionResponse from(Prescription p) {
        return new PrescriptionResponse(
                p.getId(),
                p.getPatient().getId(),
                p.getPatient().getUser().getFullName(),
                p.getDoctor().getId(),
                p.getDoctor().getUser().getFullName(),
                p.getPrescriptionDate(),
                p.getMedicineName(),
                p.getDosage(),
                p.getFrequency(),
                p.getDurationDays(),
                p.getInstructions(),
                p.getStatus().name(),
                p.getStartDate(),
                p.getEndDate()
        );
    }
}