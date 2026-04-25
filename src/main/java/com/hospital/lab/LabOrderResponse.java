package com.hospital.lab;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class LabOrderResponse {
    private UUID id;
    private UUID patientId;
    private String patientName;
    private UUID doctorId;
    private String doctorName;
    private String testName;
    private String testCode;
    private String description;
    private String status;
    private LocalDateTime orderDate;
    private LocalDateTime completedDate;
    private String results;
    private String notes;

    public static LabOrderResponse from(LabOrder l) {
        return new LabOrderResponse(
                l.getId(),
                l.getPatient().getId(),
                l.getPatient().getUser().getFullName(),
                l.getDoctor().getId(),
                l.getDoctor().getUser().getFullName(),
                l.getTestName(),
                l.getTestCode(),
                l.getDescription(),
                l.getStatus().name(),
                l.getOrderDate(),
                l.getCompletedDate(),
                l.getResults(),
                l.getNotes()
        );
    }
}