package com.hospital.medicalrecord;

import com.hospital.common.BaseEntity;
import com.hospital.appointment.Appointment;
import com.hospital.doctor.Doctor;
import com.hospital.patient.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
public class MedicalRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "visit_date", nullable = false)
    private LocalDateTime visitDate;

    @Lob
    @Column(name = "chief_complaint")
    private String chiefComplaint;

    @Lob
    @Column(name = "symptoms")
    private String symptoms;

    @Lob
    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "icd10_code")
    private String icd10Code;

    @Lob
    @Column(name = "treatment_plan")
    private String treatmentPlan;

    @Lob
    @Column(name = "follow_up_instructions")
    private String followUpInstructions;

    @Column(name = "blood_pressure")
    private String bloodPressure;

    @Column(name = "heart_rate")
    private Integer heartRate;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "height")
    private Double height;

    @Column(name = "oxygen_saturation")
    private Double oxygenSaturation;

    private String attachmentPath;
}