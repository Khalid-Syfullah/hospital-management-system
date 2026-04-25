package com.hospital.medicalrecord;

import com.hospital.common.BaseEntity;
import com.hospital.doctor.Doctor;
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "patient_id") private Patient patient;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "doctor_id") private Doctor doctor;
    private String icd10Code;
    @Column(length = 2000) private String diagnoses;
    @Column(length = 2000) private String symptoms;
    @Column(length = 4000) private String visitNotes;
    @Column(length = 2000) private String attachmentPath;
    public Patient getPatient() { return patient; } public void setPatient(Patient patient) { this.patient = patient; }
    public Doctor getDoctor() { return doctor; } public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public String getIcd10Code() { return icd10Code; } public void setIcd10Code(String icd10Code) { this.icd10Code = icd10Code; }
    public String getDiagnoses() { return diagnoses; } public void setDiagnoses(String diagnoses) { this.diagnoses = diagnoses; }
    public String getSymptoms() { return symptoms; } public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public String getVisitNotes() { return visitNotes; } public void setVisitNotes(String visitNotes) { this.visitNotes = visitNotes; }
    public String getAttachmentPath() { return attachmentPath; } public void setAttachmentPath(String attachmentPath) { this.attachmentPath = attachmentPath; }
}
