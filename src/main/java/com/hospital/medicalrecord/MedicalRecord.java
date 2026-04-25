package com.hospital.medicalrecord;

import com.hospital.common.BaseEntity;
import com.hospital.doctor.Doctor;
import com.hospital.patient.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDateTime visitDate;

    @Column(length = 500)
    private String diagnoses;

    @Column(length = 500)
    private String symptoms;

    @Column(length = 1000)
    private String visitNotes;

    @Column(length = 50)
    private String bloodPressure;

    @Column(length = 50)
    private String heartRate;

    @Column(length = 50)
    private String temperature;

    @Column(length = 50)
    private String weight;

    @Column(length = 50)
    private String height;

    @Column(length = 500)
    private String attachments;
}
