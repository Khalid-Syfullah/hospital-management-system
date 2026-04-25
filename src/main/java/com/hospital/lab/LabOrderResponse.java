package com.hospital.lab;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LabOrderResponse {
    private UUID id;
    private UUID patientId;
    private String patientName;
    private String patientMrn;
    private UUID doctorId;
    private String doctorName;
    private String testName;
    private String testCode;
    private String clinicalNotes;
    private LabStatus status;
    private String priority;
    private LabResultResponse result;
    private LocalDateTime createdAt;
}

@Data
class LabResultResponse {
    private UUID id;
    private String findings;
    private String referenceRange;
    private String interpretation;
    private LocalDateTime resultDateTime;
    private String performedBy;
}
