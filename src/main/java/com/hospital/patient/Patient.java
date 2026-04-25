package com.hospital.patient;

import com.hospital.common.BaseEntity;
import com.hospital.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "patients", indexes = @Index(name = "idx_patient_mrn", columnList = "mrn", unique = true))
public class Patient extends BaseEntity {
    public enum Gender { MALE, FEMALE, OTHER, UNKNOWN }
    @Column(nullable = false, unique = true)
    private String mrn;
    @Column(nullable = false)
    private String fullName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender = Gender.UNKNOWN;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
    @Column(length = 2000)
    private String address;
    @Column(length = 2000)
    private String medicalHistory;
    @Column(length = 1000)
    private String allergies;
    @Column(length = 1000)
    private String chronicConditions;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String insuranceProvider;
    private String insurancePolicyNumber;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public String getMrn() { return mrn; }
    public void setMrn(String mrn) { this.mrn = mrn; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public String getChronicConditions() { return chronicConditions; }
    public void setChronicConditions(String chronicConditions) { this.chronicConditions = chronicConditions; }
    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
    public String getInsuranceProvider() { return insuranceProvider; }
    public void setInsuranceProvider(String insuranceProvider) { this.insuranceProvider = insuranceProvider; }
    public String getInsurancePolicyNumber() { return insurancePolicyNumber; }
    public void setInsurancePolicyNumber(String insurancePolicyNumber) { this.insurancePolicyNumber = insurancePolicyNumber; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
