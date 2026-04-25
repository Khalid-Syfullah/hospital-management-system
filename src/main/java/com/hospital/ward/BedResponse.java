package com.hospital.ward;

import java.util.UUID;

public record BedResponse(UUID id, UUID wardId, String bedNumber, Bed.Status status, UUID patientId) {
    static BedResponse from(Bed b) { return new BedResponse(b.getId(), b.getWard().getId(), b.getBedNumber(), b.getStatus(), b.getPatient() == null ? null : b.getPatient().getId()); }
}
