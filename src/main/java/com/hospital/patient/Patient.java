package com.hospital.patient;

import com.hospital.common.BaseEntity;
import com.hospital.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String mrn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String fullName;

    private LocalDate dateOfBirth;
    private String phone;
    private String emergencyContact;
    private String insuranceProvider;
    private String insurancePolicyNumber;
    @Column(length = 2000)
    private String medicalHistory;
    @Column(length = 1000)
    private String allergies;
    @Column(length = 1000)
    private String chronicConditions;

    protected Patient() {
    }

    public Patient(String mrn, String fullName, LocalDate dateOfBirth, String phone) {
        this.mrn = mrn;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
    }

    public String getMrn() {
        return mrn;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public String getInsurancePolicyNumber() {
        return insurancePolicyNumber;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getChronicConditions() {
        return chronicConditions;
    }

    public void update(String fullName, LocalDate dateOfBirth, String phone, String emergencyContact,
            String insuranceProvider, String insurancePolicyNumber, String medicalHistory, String allergies,
            String chronicConditions) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.insuranceProvider = insuranceProvider;
        this.insurancePolicyNumber = insurancePolicyNumber;
        this.medicalHistory = medicalHistory;
        this.allergies = allergies;
        this.chronicConditions = chronicConditions;
    }
}
