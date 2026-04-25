package com.hospital.appointment;

import com.hospital.common.BaseEntity;
import com.hospital.doctor.Doctor;
import com.hospital.patient.Patient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.OffsetDateTime;

@Entity
@Table(name = "appointments", indexes = {
        @Index(name = "idx_appointment_doctor_start", columnList = "doctor_id,startTime"),
        @Index(name = "idx_appointment_date", columnList = "startTime"),
        @Index(name = "idx_appointment_idempotency", columnList = "idempotencyKey", unique = true)
})
public class Appointment extends BaseEntity {
    public enum Status { SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW }
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "patient_id") private Patient patient;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "doctor_id") private Doctor doctor;
    @Column(nullable = false) private OffsetDateTime startTime;
    @Column(nullable = false) private OffsetDateTime endTime;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Status status = Status.SCHEDULED;
    @Column(nullable = false, unique = true) private String idempotencyKey;
    private String reason;
    @Version private long version;
    public Patient getPatient() { return patient; } public void setPatient(Patient patient) { this.patient = patient; }
    public Doctor getDoctor() { return doctor; } public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public OffsetDateTime getStartTime() { return startTime; } public void setStartTime(OffsetDateTime startTime) { this.startTime = startTime; }
    public OffsetDateTime getEndTime() { return endTime; } public void setEndTime(OffsetDateTime endTime) { this.endTime = endTime; }
    public Status getStatus() { return status; } public void setStatus(Status status) { this.status = status; }
    public String getIdempotencyKey() { return idempotencyKey; } public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public String getReason() { return reason; } public void setReason(String reason) { this.reason = reason; }
    public long getVersion() { return version; } public void setVersion(long version) { this.version = version; }
}
