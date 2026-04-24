package com.hospital.ward;

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
@Table(name = "beds")
public class Bed extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ward_id")
    private Ward ward;
    @Column(nullable = false)
    private String bedNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BedStatus status = BedStatus.AVAILABLE;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    protected Bed() {
    }

    public Bed(Ward ward, String bedNumber) {
        this.ward = ward;
        this.bedNumber = bedNumber;
    }

    public Ward getWard() {
        return ward;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public BedStatus getStatus() {
        return status;
    }

    public Patient getPatient() {
        return patient;
    }

    public void admit(Patient patient) {
        if (status != BedStatus.AVAILABLE) {
            throw new IllegalArgumentException("Bed is not available");
        }
        this.patient = patient;
        this.status = BedStatus.OCCUPIED;
    }

    public void discharge() {
        this.patient = null;
        this.status = BedStatus.AVAILABLE;
    }
}
