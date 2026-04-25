package com.hospital.appointment;

import com.hospital.doctor.Doctor;
import com.hospital.doctor.DoctorService;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.SlotUnavailableException;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientService;
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
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Transactional
    public Appointment createAppointment(AppointmentCreateRequest request) {
        if (request.getIdempotencyKey() != null) {
            appointmentRepository.findByIdempotencyKey(request.getIdempotencyKey())
                    .ifPresent(existing -> {
                        log.info("Returning existing appointment for idempotency key: {}", request.getIdempotencyKey());
                    });
        }

        Patient patient = patientService.getPatientById(request.getPatientId());
        Doctor doctor = doctorService.getDoctorById(request.getDoctorId());

        validateSlotAvailability(doctor, request.getAppointmentDate(), request.getSlotDurationMinutes());

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setEndTime(request.getAppointmentDate().plusMinutes(request.getSlotDurationMinutes() != null ?
                request.getSlotDurationMinutes() : doctor.getSlotDurationMinutes()));
        appointment.setReasonForVisit(request.getReasonForVisit());
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        appointment.setIdempotencyKey(request.getIdempotencyKey());

        appointment = appointmentRepository.save(appointment);
        log.info("Appointment created: {} for patient {} with doctor {}", appointment.getId(), patient.getMrn(), doctor.getLicenseNumber());
        return appointment;
    }

    private void validateSlotAvailability(Doctor doctor, LocalDateTime dateTime, Integer durationMinutes) {
        int duration = durationMinutes != null ? durationMinutes : doctor.getSlotDurationMinutes();
        LocalDateTime endTime = dateTime.plusMinutes(duration);

        List<Appointment> conflictingAppointments = appointmentRepository.findByDoctorIdAndDateRange(
                doctor.getId(),
                dateTime.minusMinutes(1),
                endTime.plusMinutes(1)
        );

        if (!conflictingAppointments.isEmpty()) {
            throw new SlotUnavailableException("Time slot is not available for this doctor");
        }
    }

    public Appointment getAppointmentById(UUID id) {
        return appointmentRepository.findById(id)
                .filter(a -> !a.isDeleted())
                .orElseThrow(() -> new BadRequestException("Appointment not found"));
    }

    public List<Appointment> getAppointmentsByPatient(UUID patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(UUID doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public Page<Appointment> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAllActive(pageable);
    }

    public Page<Appointment> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return appointmentRepository.findByDateRange(start, end, pageable);
    }

    @Transactional
    public Appointment confirmAppointment(UUID id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);
        log.info("Appointment confirmed: {}", appointment.getId());
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment startAppointment(UUID id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(Appointment.AppointmentStatus.IN_PROGRESS);
        log.info("Appointment started: {}", appointment.getId());
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment completeAppointment(UUID id, String notes) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        appointment.setNotes(notes);
        log.info("Appointment completed: {}", appointment.getId());
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment rescheduleAppointment(UUID id, AppointmentRescheduleRequest request) {
        Appointment appointment = getAppointmentById(id);

        if (appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Cannot reschedule a cancelled appointment");
        }

        validateSlotAvailability(appointment.getDoctor(), request.getNewAppointmentDate(), null);

        appointment.setAppointmentDate(request.getNewAppointmentDate());
        appointment.setEndTime(request.getNewAppointmentDate().plusMinutes(appointment.getDoctor().getSlotDurationMinutes()));
        appointment.setRescheduled(true);
        if (request.getReason() != null) {
            appointment.setNotes((appointment.getNotes() != null ? appointment.getNotes() + "\n" : "") +
                    "Rescheduled: " + request.getReason());
        }

        log.info("Appointment rescheduled: {} to {}", appointment.getId(), request.getNewAppointmentDate());
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment cancelAppointment(UUID id, String reason) {
        Appointment appointment = getAppointmentById(id);

        if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed appointment");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(reason);

        log.info("Appointment cancelled: {} reason: {}", appointment.getId(), reason);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public void markNoShow(UUID id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(Appointment.AppointmentStatus.NO_SHOW);
        appointmentRepository.save(appointment);
        log.info("Appointment marked as no-show: {}", appointment.getId());
    }
}