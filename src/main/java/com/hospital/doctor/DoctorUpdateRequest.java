package com.hospital.doctor;

import lombok.Data;

import java.util.UUID;

@Data
public class DoctorUpdateRequest {
    private String specialization;
    private String qualification;
    private String biography;
    private UUID departmentId;
    private Double consultationFee;
    private Double followUpFee;
    private Integer slotDurationMinutes;
    private Integer maxPatientsPerDay;
}