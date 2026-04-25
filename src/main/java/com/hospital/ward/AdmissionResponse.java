package com.hospital.ward;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AdmissionResponse {
    private UUID id;
    private UUID patientId;
    private String patientName;
    private String patientMrn;
    private UUID bedId;
    private String bedNumber;
    private String wardName;
    private UUID doctorId;
    private String doctorName;
    private LocalDateTime admissionTime;
    private LocalDateTime dischargeTime;
    private String admissionReason;
    private String dischargeNotes;
    private boolean active;
}
