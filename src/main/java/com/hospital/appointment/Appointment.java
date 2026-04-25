package com.hospital.appointment;

import com.hospital.common.BaseEntity;
import com.hospital.doctor.Doctor;
import com.hospital.patient.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class Appointment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(name = "reason_for_visit")
    private String reasonForVisit;

    @Lob
    @Column(name = "notes")
    private String notes;

    @Column(name = "is_rescheduled")
    private boolean rescheduled = false;

    @Column(name = "cancelled_by")
    private String cancelledBy;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    public boolean isRescheduled() {
        return rescheduled;
    }

    public void setRescheduled(boolean rescheduled) {
        this.rescheduled = rescheduled;
    }

    public enum AppointmentStatus {
        SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
    }
}