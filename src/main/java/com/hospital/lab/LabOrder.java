package com.hospital.lab;

import com.hospital.common.BaseEntity;
import com.hospital.patient.Patient;
import jakarta.persistence.*;

@Entity
@Table(name = "lab_orders")
public class LabOrder extends BaseEntity {
    public enum Status { REQUESTED, IN_PROGRESS, COMPLETED, CANCELLED }
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "patient_id") private Patient patient;
    @Column(nullable = false) private String testName;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Status status = Status.REQUESTED;
    @Column(length = 4000) private String result;
    public Patient getPatient() { return patient; } public void setPatient(Patient patient) { this.patient = patient; }
    public String getTestName() { return testName; } public void setTestName(String testName) { this.testName = testName; }
    public Status getStatus() { return status; } public void setStatus(Status status) { this.status = status; }
    public String getResult() { return result; } public void setResult(String result) { this.result = result; }
}
