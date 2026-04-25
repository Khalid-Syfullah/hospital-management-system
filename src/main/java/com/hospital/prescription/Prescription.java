package com.hospital.prescription;

import com.hospital.common.BaseEntity;
import com.hospital.doctor.Doctor;
import com.hospital.patient.Patient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "prescriptions")
public class Prescription extends BaseEntity {
    public enum Status { ACTIVE, COMPLETED, CANCELLED }
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "patient_id") private Patient patient;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "doctor_id") private Doctor doctor;
    @Column(nullable = false) private String medicineName;
    @Column(nullable = false) private String dosage;
    @Column(nullable = false) private String frequency;
    @Column(nullable = false) private String duration;
    @Column(length = 2000) private String instructions;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Status status = Status.ACTIVE;
    public Patient getPatient() { return patient; } public void setPatient(Patient patient) { this.patient = patient; }
    public Doctor getDoctor() { return doctor; } public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public String getMedicineName() { return medicineName; } public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public String getDosage() { return dosage; } public void setDosage(String dosage) { this.dosage = dosage; }
    public String getFrequency() { return frequency; } public void setFrequency(String frequency) { this.frequency = frequency; }
    public String getDuration() { return duration; } public void setDuration(String duration) { this.duration = duration; }
    public String getInstructions() { return instructions; } public void setInstructions(String instructions) { this.instructions = instructions; }
    public Status getStatus() { return status; } public void setStatus(Status status) { this.status = status; }
}
