package com.hospital.patient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotNull
    @Past
    private LocalDate dateOfBirth;

    @NotNull
    private Gender gender;

    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 20)
    private String phoneNumber;

    private String address;

    @Size(max = 50)
    private String bloodType;

    private String medicalHistory;

    private String chronicConditions;

    @Valid
    private EmergencyContact emergencyContact;

    @Valid
    private InsuranceInfo insuranceInfo;
}
