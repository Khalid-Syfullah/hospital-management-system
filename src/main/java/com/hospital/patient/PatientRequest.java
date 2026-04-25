package com.hospital.patient;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

public record PatientRequest(@NotBlank String fullName, Patient.Gender gender, LocalDate dateOfBirth, String phone,
                             String email, String address, String medicalHistory, String allergies,
                             String chronicConditions, String emergencyContactName, String emergencyContactPhone,
                             String insuranceProvider, String insurancePolicyNumber, UUID userId) {}
