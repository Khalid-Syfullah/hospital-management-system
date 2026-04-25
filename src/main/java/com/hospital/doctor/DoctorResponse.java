package com.hospital.doctor;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DoctorResponse {
    private UUID id;
    private UUID userId;
    private String doctorName;
    private String email;
    private UUID departmentId;
    private String departmentName;
    private String specialization;
    private String licenseNumber;
    private String qualification;
    private Integer yearsOfExperience;
    private String biography;
    private String consultationFee;
    private LocalDateTime createdAt;
}
