package com.hospital.patient;

import java.time.LocalDate;
import java.util.UUID;

public record PatientResponse(UUID id, String mrn, String fullName, Patient.Gender gender, LocalDate dateOfBirth,
                              String phone, String email, String allergies, String chronicConditions,
                              String insuranceProvider) {
    static PatientResponse from(Patient p) {
        return new PatientResponse(p.getId(), p.getMrn(), p.getFullName(), p.getGender(), p.getDateOfBirth(),
                p.getPhone(), p.getEmail(), p.getAllergies(), p.getChronicConditions(), p.getInsuranceProvider());
    }
}
