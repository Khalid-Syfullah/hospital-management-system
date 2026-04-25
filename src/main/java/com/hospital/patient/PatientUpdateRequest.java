package com.hospital.patient;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientUpdateRequest {
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelationship;
    private String medicalHistory;
    private String allergies;
    private String chronicConditions;
    private String insuranceProvider;
    private String insurancePolicyNumber;
    private LocalDate insuranceExpiryDate;
}