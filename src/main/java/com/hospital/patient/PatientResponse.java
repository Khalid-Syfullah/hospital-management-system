package com.hospital.patient;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PatientResponse {
    private UUID id;
    private String mrn;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private String address;
    private String bloodType;
    private String medicalHistory;
    private String chronicConditions;
    private EmergencyContact emergencyContact;
    private InsuranceInfo insuranceInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
