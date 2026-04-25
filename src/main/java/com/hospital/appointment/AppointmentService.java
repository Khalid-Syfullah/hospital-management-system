package com.hospital.appointment;

import com.hospital.doctor.Doctor;
import com.hospital.doctor.DoctorRepository;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ConflictException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;

    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        // Idempotency check
        if (request.getIdempotencyKey() != null) {
            var existing = appointmentRepository.findByIdempotencyKey(request.getIdempotencyKey());
            if (existing.isPresent()) {
                log.info("Appointment already booked with idempotency key: {}", request.getIdempotencyKey());
                return appointmentMapper.toResponse(existing.get());
            }
        }

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        // Check for double-booking
        LocalDateTime appointmentEnd = request.getAppointmentDateTime()
                .plusMinutes(request.getDurationMinutes());

        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
                doctor,
                request.getAppointmentDateTime(),
                appointmentEnd
        );

        if (!conflicts.isEmpty()) {
            log.warn("Conflict detected for doctor: {} at time: {}", doctor.getId(), request.getAppointmentDateTime());
            throw new ConflictException("Doctor is not available at the requested time");
        }

        Appointment appointment = appointmentMapper.toEntity(request);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        appointment.setCreatedBy("system");
        appointment.setUpdatedBy("system");

        appointment = appointmentRepository.save(appointment);
        log.info("Appointment booked successfully: {}", appointment.getId());

        return appointmentMapper.toResponse(appointment);
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    public AppointmentResponse updateAppointment(UUID id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!appointment.getStatus().equals(Appointment.AppointmentStatus.SCHEDULED)) {
            throw new BadRequestException("Can only reschedule appointments in SCHEDULED status");
        }

        Doctor doctor = appointment.getDoctor();

        // Check for double-booking on new time
        LocalDateTime appointmentEnd = request.getAppointmentDateTime()
                .plusMinutes(request.getDurationMinutes());

        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
                doctor,
                request.getAppointmentDateTime(),
                appointmentEnd
        ).stream()
                .filter(a -> !a.getId().equals(id))
                .collect(Collectors.toList());

        if (!conflicts.isEmpty()) {
            throw new ConflictException("Doctor is not available at the requested time");
        }

        appointment.setAppointmentDateTime(request.getAppointmentDateTime());
        appointment.setDurationMinutes(request.getDurationMinutes());
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());
        appointment.setUpdatedBy("system");

        appointment = appointmentRepository.save(appointment);
        log.info("Appointment rescheduled: {}", id);

        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    public void cancelAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointment.getStatus().equals(Appointment.AppointmentStatus.CANCELLED)) {
            throw new BadRequestException("Appointment is already cancelled");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointment.setUpdatedBy("system");
        appointmentRepository.save(appointment);
        log.info("Appointment cancelled: {}", id);
    }

    @Transactional
    public AppointmentResponse confirmAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!appointment.getStatus().equals(Appointment.AppointmentStatus.SCHEDULED)) {
            throw new BadRequestException("Can only confirm appointments in SCHEDULED status");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);
        appointment.setUpdatedBy("system");
        appointment = appointmentRepository.save(appointment);
        log.info("Appointment confirmed: {}", id);

        return appointmentMapper.toResponse(appointment);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getPatientAppointments(UUID patientId, Pageable pageable) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Page<Appointment> appointments = appointmentRepository.findByPatient(patient, pageable);
        List<AppointmentResponse> responses = appointments.getContent().stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, appointments.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getDoctorAppointments(UUID doctorId, Pageable pageable) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Page<Appointment> appointments = appointmentRepository.findByDoctor(doctor, pageable);
        List<AppointmentResponse> responses = appointments.getContent().stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, appointments.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAppointmentsByStatus(Appointment.AppointmentStatus status, Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.findByStatus(status, pageable);
        List<AppointmentResponse> responses = appointments.getContent().stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, appointments.getTotalElements());
    }
}
