package com.hospital.appointment;

import com.hospital.audit.AuditService;
import com.hospital.doctor.DoctorService;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.exception.SlotUnavailableException;
import com.hospital.notification.NotificationService;
import com.hospital.patient.PatientService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {
    private final AppointmentRepository repository;
    private final PatientService patients;
    private final DoctorService doctors;
    private final NotificationService notifications;
    private final AuditService audit;
    public AppointmentService(AppointmentRepository repository, PatientService patients, DoctorService doctors,
                              NotificationService notifications, AuditService audit) {
        this.repository = repository; this.patients = patients; this.doctors = doctors; this.notifications = notifications; this.audit = audit;
    }
    @Transactional(readOnly = true) public Page<AppointmentResponse> list(Pageable pageable) { return repository.findAll(pageable).map(AppointmentResponse::from); }
    @Transactional(readOnly = true) public AppointmentResponse get(UUID id) { return AppointmentResponse.from(find(id)); }
    @Transactional public AppointmentResponse book(AppointmentRequest request, String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) throw new BadRequestException("Idempotency-Key header is required");
        var existing = repository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) return AppointmentResponse.from(existing.get());
        validateSlot(request.doctorId(), request.startTime(), request.endTime());
        Appointment appointment = new Appointment();
        appointment.setPatient(patients.find(request.patientId()));
        appointment.setDoctor(doctors.find(request.doctorId()));
        appointment.setStartTime(request.startTime()); appointment.setEndTime(request.endTime());
        appointment.setReason(request.reason()); appointment.setIdempotencyKey(idempotencyKey);
        repository.save(appointment);
        audit.record("Appointment", appointment.getId().toString(), "CREATE", "booked");
        notifications.queue("APPOINTMENT_REMINDER", "Appointment booked", "Appointment " + appointment.getId() + " was booked");
        return AppointmentResponse.from(appointment);
    }
    @Transactional public AppointmentResponse reschedule(UUID id, AppointmentRequest request) {
        Appointment appointment = find(id);
        validateSlot(request.doctorId(), request.startTime(), request.endTime());
        appointment.setDoctor(doctors.find(request.doctorId()));
        appointment.setStartTime(request.startTime()); appointment.setEndTime(request.endTime()); appointment.setReason(request.reason());
        audit.record("Appointment", id.toString(), "UPDATE", "rescheduled");
        return AppointmentResponse.from(appointment);
    }
    @Transactional public AppointmentResponse cancel(UUID id) {
        Appointment appointment = find(id); appointment.setStatus(Appointment.Status.CANCELLED);
        audit.record("Appointment", id.toString(), "UPDATE", "cancelled"); return AppointmentResponse.from(appointment);
    }
    private void validateSlot(UUID doctorId, java.time.OffsetDateTime start, java.time.OffsetDateTime end) {
        if (!end.isAfter(start)) throw new BadRequestException("Appointment end time must be after start time");
        if (repository.hasConflict(doctorId, start, end)) throw new SlotUnavailableException("Doctor slot is unavailable");
    }
    private Appointment find(UUID id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment", id)); }
}
