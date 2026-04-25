package com.hospital.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AppointmentResponse {
    private UUID id;
    private UUID patientId;
    private String patientMrn;
    private String patientName;
    private UUID doctorId;
    private String doctorName;
    private LocalDateTime appointmentDate;
    private LocalDateTime endTime;
    private String status;
    private String reasonForVisit;
    private String notes;
    private boolean isRescheduled;
    private String cancellationReason;

    public static AppointmentResponse from(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getPatient().getId(),
                a.getPatient().getMrn(),
                a.getPatient().getUser().getFullName(),
                a.getDoctor().getId(),
                a.getDoctor().getUser().getFullName(),
                a.getAppointmentDate(),
                a.getEndTime(),
                a.getStatus().name(),
                a.getReasonForVisit(),
                a.getNotes(),
                a.isRescheduled(),
                a.getCancellationReason()
        );
    }
}