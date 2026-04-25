package com.hospital.appointment;

import com.hospital.doctor.Doctor;
import com.hospital.doctor.DoctorRepository;
import com.hospital.exception.AppointmentNotFoundException;
import com.hospital.exception.DoctorNotFoundException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.exception.SlotUnavailableException;
import com.hospital.notification.NotificationService;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;
    private final NotificationService notificationService;

    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        if (request.getIdempotencyKey() != null) {
            var existing = appointmentRepository.findByIdempotencyKey(request.getIdempotencyKey());
            if (existing.isPresent()) {
                return appointmentMapper.toResponse(existing.get());
            }
        }

        Patient patient = patientRepository.findByIdAndDeletedAtIsNull(request.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException(request.getPatientId()));

        Doctor doctor = doctorRepository.findByIdAndDeletedAtIsNull(request.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException(request.getDoctorId()));

        validateNoConflict(request.getDoctorId(), request.getStartTime(), request.getEndTime(), null);

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(AppointmentStatus.SCHEDULED)
                .reason(request.getReason())
                .notes(request.getNotes())
                .idempotencyKey(request.getIdempotencyKey())
                .build();

        appointment = appointmentRepository.save(appointment);
        notificationService.sendAppointmentConfirmation(appointment);
        log.info("Appointment booked: {} for patient {}", appointment.getId(), patient.getMrn());
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    public AppointmentResponse rescheduleAppointment(UUID id, LocalDateTime newStart, LocalDateTime newEnd) {
        Appointment appointment = findActiveAppointment(id);

        if (appointment.getStatus() == AppointmentStatus.COMPLETED ||
            appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot reschedule a " + appointment.getStatus() + " appointment");
        }

        validateNoConflict(appointment.getDoctor().getId(), newStart, newEnd, id);

        appointment.setStartTime(newStart);
        appointment.setEndTime(newEnd);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse cancelAppointment(UUID id, String reason) {
        Appointment appointment = findActiveAppointment(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(reason);
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse updateStatus(UUID id, AppointmentStatus status) {
        Appointment appointment = findActiveAppointment(id);
        appointment.setStatus(status);
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(UUID id) {
        return appointmentMapper.toResponse(findActiveAppointment(id));
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAppointmentsByPatient(UUID patientId, Pageable pageable) {
        return appointmentRepository.findByPatientIdAndDeletedAtIsNull(patientId, pageable)
                .map(appointmentMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAppointmentsByDoctor(UUID doctorId, Pageable pageable) {
        return appointmentRepository.findByDoctorIdAndDeletedAtIsNull(doctorId, pageable)
                .map(appointmentMapper::toResponse);
    }

    private void validateNoConflict(UUID doctorId, LocalDateTime start, LocalDateTime end, UUID excludeId) {
        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(doctorId, start, end);
        if (excludeId != null) {
            conflicts = conflicts.stream().filter(a -> !a.getId().equals(excludeId)).toList();
        }
        if (!conflicts.isEmpty()) {
            throw new SlotUnavailableException("Doctor has a conflicting appointment at the requested time");
        }
    }

    private Appointment findActiveAppointment(UUID id) {
        return appointmentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
    }
}
