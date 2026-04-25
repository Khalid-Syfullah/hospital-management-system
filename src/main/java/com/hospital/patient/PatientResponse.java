package com.hospital.patient;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PatientResponse {
    private UUID id;
    private String mrn;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodType;
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
    private LocalDateTime createdAt;

    public static PatientResponse from(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getMrn(),
                patient.getUser().getEmail(),
                patient.getUser().getFirstName(),
                patient.getUser().getLastName(),
                patient.getUser().getPhone(),
                patient.getDateOfBirth(),
                patient.getGender() != null ? patient.getGender().name() : null,
                patient.getBloodType(),
                patient.getAddress(),
                patient.getCity(),
                patient.getState(),
                patient.getZipCode(),
                patient.getCountry(),
                patient.getEmergencyContactName(),
                patient.getEmergencyContactPhone(),
                patient.getEmergencyContactRelationship(),
                patient.getMedicalHistory(),
                patient.getAllergies(),
                patient.getChronicConditions(),
                patient.getInsuranceProvider(),
                patient.getInsurancePolicyNumber(),
                patient.getInsuranceExpiryDate(),
                patient.getCreatedAt()
        );
    }
}