package com.hospital.lab;

import com.hospital.common.BaseEntity;
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
@Table(name = "lab_orders")
public class LabOrder extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @Column(nullable = false)
    private String testName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LabOrderStatus status = LabOrderStatus.REQUESTED;
    @Column(length = 4000)
    private String result;

    protected LabOrder() {
    }

    public LabOrder(Patient patient, String testName) {
        this.patient = patient;
        this.testName = testName;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getTestName() {
        return testName;
    }

    public LabOrderStatus getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }

    public void complete(String result) {
        this.result = result;
        this.status = LabOrderStatus.COMPLETED;
    }
}
