package com.hospital.appointment;

import com.hospital.appointment.AppointmentDtos.AppointmentResponse;
import com.hospital.appointment.AppointmentDtos.BookAppointmentRequest;
import com.hospital.appointment.AppointmentDtos.RescheduleAppointmentRequest;
import com.hospital.audit.AuditService;
import com.hospital.doctor.Doctor;
import com.hospital.doctor.DoctorRepository;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.exception.SlotUnavailableException;
import com.hospital.notification.NotificationService;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;
    private final NotificationService notificationService;
    private final AuditService auditService;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientService patientService,
            DoctorRepository doctorRepository, AppointmentMapper appointmentMapper,
            NotificationService notificationService, AuditService auditService) {
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
        this.doctorRepository = doctorRepository;
        this.appointmentMapper = appointmentMapper;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    public AppointmentResponse book(BookAppointmentRequest request, String idempotencyKey) {
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            var existing = appointmentRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return appointmentMapper.toResponse(existing.get());
            }
        }
        validateTimeRange(request.startTime(), request.endTime());
        if (appointmentRepository.existsOverlappingAppointment(request.doctorId(), request.startTime(), request.endTime())) {
            throw new SlotUnavailableException("Selected appointment slot is unavailable");
        }
        Patient patient = patientService.findActive(request.patientId());
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        Appointment appointment = appointmentRepository.save(new Appointment(patient, doctor, request.startTime(), request.endTime(), idempotencyKey));
        auditService.record("Appointment", appointment.getId().toString(), "CREATE", "Appointment booked", null);
        notificationService.enqueue("APPOINTMENT_BOOKED", "Appointment booked for " + appointment.getStartTime(), null);
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST')")
    public Page<AppointmentResponse> list(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(appointmentMapper::toResponse);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public AppointmentResponse reschedule(UUID id, RescheduleAppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        validateTimeRange(request.startTime(), request.endTime());
        if (appointmentRepository.existsOverlappingAppointment(appointment.getDoctor().getId(), request.startTime(), request.endTime())) {
            throw new SlotUnavailableException("Selected appointment slot is unavailable");
        }
        appointment.reschedule(request.startTime(), request.endTime());
        auditService.record("Appointment", id.toString(), "UPDATE", "Appointment rescheduled", null);
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    public AppointmentResponse cancel(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        appointment.cancel();
        auditService.record("Appointment", id.toString(), "CANCEL", "Appointment cancelled", null);
        return appointmentMapper.toResponse(appointment);
    }

    private void validateTimeRange(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Appointment end time must be after start time");
        }
    }
}
