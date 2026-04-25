package com.hospital.ward;

import com.hospital.common.BaseEntity;
import com.hospital.patient.Patient;
import jakarta.persistence.*;

@Entity
@Table(name = "beds")
public class Bed extends BaseEntity {
    public enum Status { AVAILABLE, OCCUPIED, MAINTENANCE }
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "ward_id") private Ward ward;
    @Column(nullable = false) private String bedNumber;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Status status = Status.AVAILABLE;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id") private Patient patient;
    public Ward getWard() { return ward; } public void setWard(Ward ward) { this.ward = ward; }
    public String getBedNumber() { return bedNumber; } public void setBedNumber(String bedNumber) { this.bedNumber = bedNumber; }
    public Status getStatus() { return status; } public void setStatus(Status status) { this.status = status; }
    public Patient getPatient() { return patient; } public void setPatient(Patient patient) { this.patient = patient; }
}
