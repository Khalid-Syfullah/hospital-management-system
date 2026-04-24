package com.hospital.medicalrecord;

import com.hospital.common.BaseEntity;
import com.hospital.patient.Patient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "medical_records")
public class MedicalRecord extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    private String icd10Code;
    @Column(length = 2000)
    private String symptoms;
    @Column(length = 4000)
    private String visitNotes;
    private String vitals;
    private String labResultReference;
    private String attachmentPath;

    protected MedicalRecord() {
    }

    public MedicalRecord(Patient patient, String icd10Code, String symptoms, String visitNotes, String vitals) {
        this.patient = patient;
        this.icd10Code = icd10Code;
        this.symptoms = symptoms;
        this.visitNotes = visitNotes;
        this.vitals = vitals;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getIcd10Code() {
        return icd10Code;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getVisitNotes() {
        return visitNotes;
    }

    public String getVitals() {
        return vitals;
    }

    public String getLabResultReference() {
        return labResultReference;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }
}
