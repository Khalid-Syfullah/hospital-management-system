package com.hospital.medicalrecord;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MedicalRecordResponse {
    private UUID id;
    private UUID patientId;
    private String patientMrn;
    private String patientName;
    private UUID doctorId;
    private String doctorName;
    private LocalDateTime visitDate;
    private String chiefComplaint;
    private String symptoms;
    private String diagnosis;
    private String icd10Code;
    private String treatmentPlan;
    private String followUpInstructions;
    private String bloodPressure;
    private Integer heartRate;
    private Double temperature;
    private Double weight;
    private Double height;
    private Double oxygenSaturation;

    public static MedicalRecordResponse from(MedicalRecord r) {
        return new MedicalRecordResponse(
                r.getId(),
                r.getPatient().getId(),
                r.getPatient().getMrn(),
                r.getPatient().getUser().getFullName(),
                r.getDoctor().getId(),
                r.getDoctor().getUser().getFullName(),
                r.getVisitDate(),
                r.getChiefComplaint(),
                r.getSymptoms(),
                r.getDiagnosis(),
                r.getIcd10Code(),
                r.getTreatmentPlan(),
                r.getFollowUpInstructions(),
                r.getBloodPressure(),
                r.getHeartRate(),
                r.getTemperature(),
                r.getWeight(),
                r.getHeight(),
                r.getOxygenSaturation()
        );
    }
}