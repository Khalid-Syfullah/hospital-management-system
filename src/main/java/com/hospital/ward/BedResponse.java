package com.hospital.ward;

import com.hospital.patient.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BedResponse {
    private UUID id;
    private UUID wardId;
    private String wardName;
    private String bedNumber;
    private String status;
    private UUID patientId;
    private String patientName;
    private LocalDateTime admittedAt;
    private LocalDateTime dischargedAt;

    public static BedResponse from(Bed b) {
        Patient p = b.getPatient();
        return new BedResponse(
                b.getId(),
                b.getWard().getId(),
                b.getWard().getName(),
                b.getBedNumber(),
                b.getStatus().name(),
                p != null ? p.getId() : null,
                p != null ? p.getUser().getFullName() : null,
                b.getAdmittedAt(),
                b.getDischargedAt()
        );
    }
}