package com.hospital.medicalrecord;

import java.util.UUID;

public record MedicalRecordResponse(UUID id, UUID patientId, UUID doctorId, String icd10Code, String diagnoses,
                                    String symptoms, String visitNotes, String attachmentPath) {
    static MedicalRecordResponse from(MedicalRecord r) {
        return new MedicalRecordResponse(r.getId(), r.getPatient().getId(), r.getDoctor().getId(), r.getIcd10Code(), r.getDiagnoses(), r.getSymptoms(), r.getVisitNotes(), r.getAttachmentPath());
    }
}
