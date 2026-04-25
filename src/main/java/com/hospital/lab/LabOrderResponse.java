package com.hospital.lab;

import java.util.UUID;

public record LabOrderResponse(UUID id, UUID patientId, String testName, LabOrder.Status status, String result) {
    static LabOrderResponse from(LabOrder l) { return new LabOrderResponse(l.getId(), l.getPatient().getId(), l.getTestName(), l.getStatus(), l.getResult()); }
}
