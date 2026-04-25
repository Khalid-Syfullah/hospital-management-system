package com.hospital.doctor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class DoctorRequest {

    @NotNull
    private UUID userId;

    private UUID departmentId;

    @Size(max = 100)
    private String specialization;

    @Size(max = 50)
    private String licenseNumber;

    @Size(max = 100)
    private String qualification;

    private Integer yearsOfExperience;

    private String biography;

    @Size(max = 100)
    private String consultationFee;
}
