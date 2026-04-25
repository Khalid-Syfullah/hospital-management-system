package com.hospital.lab;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LabResultRequest {
    private String findings;
    private String referenceRange;
    private String interpretation;
    private LocalDateTime resultDateTime;
    private String performedBy;
    private String attachmentPath;
}
