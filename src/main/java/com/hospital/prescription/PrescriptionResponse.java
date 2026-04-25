package com.hospital.prescription;

import java.util.UUID;

public record PrescriptionResponse(UUID id, UUID patientId, UUID doctorId, String medicineName, String dosage,
                                   String frequency, String duration, String instructions, Prescription.Status status) {
    static PrescriptionResponse from(Prescription p) { return new PrescriptionResponse(p.getId(), p.getPatient().getId(), p.getDoctor().getId(), p.getMedicineName(), p.getDosage(), p.getFrequency(), p.getDuration(), p.getInstructions(), p.getStatus()); }
}
