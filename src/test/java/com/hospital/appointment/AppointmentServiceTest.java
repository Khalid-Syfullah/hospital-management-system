package com.hospital.appointment;

import com.hospital.doctor.Doctor;
import com.hospital.doctor.DoctorRepository;
import com.hospital.exception.AppointmentNotFoundException;
import com.hospital.exception.SlotUnavailableException;
import com.hospital.notification.NotificationService;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientRepository;
import com.hospital.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private NotificationService notificationService;

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService(
                appointmentRepository, patientRepository, doctorRepository,
                new AppointmentMapperImpl(), notificationService);
    }

    private Patient buildPatient() {
        return Patient.builder()
                .mrn("MRN0000001")
                .firstName("John")
                .lastName("Patient")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
    }

    private Doctor buildDoctor() {
        User user = User.builder()
                .email("doctor@example.com")
                .firstName("Dr")
                .lastName("Smith")
                .build();
        return Doctor.builder()
                .user(user)
                .specialization("General")
                .build();
    }

    @Test
    @DisplayName("Book appointment succeeds when no conflict")
    void bookAppointment_success() {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(UUID.randomUUID());
        request.setDoctorId(UUID.randomUUID());
        request.setStartTime(LocalDateTime.now().plusDays(1));
        request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        Patient patient = buildPatient();
        Doctor doctor = buildDoctor();

        when(patientRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(patient));
        when(doctorRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findConflictingAppointments(any(), any(), any())).thenReturn(Collections.emptyList());

        Appointment saved = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(appointmentRepository.save(any())).thenReturn(saved);
        doNothing().when(notificationService).sendAppointmentConfirmation(any());

        AppointmentResponse response = appointmentService.bookAppointment(request);

        assertThat(response.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
        assertThat(response.getDoctorName()).isEqualTo("Dr Smith");
    }

    @Test
    @DisplayName("Book appointment returns existing on idempotency key match")
    void bookAppointment_idempotencyKey_returnsExisting() {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(UUID.randomUUID());
        request.setDoctorId(UUID.randomUUID());
        request.setStartTime(LocalDateTime.now().plusDays(1));
        request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
        request.setIdempotencyKey("idem-key-001");

        Patient patient = buildPatient();
        Doctor doctor = buildDoctor();

        Appointment existing = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(AppointmentStatus.SCHEDULED)
                .idempotencyKey("idem-key-001")
                .build();

        when(appointmentRepository.findByIdempotencyKey("idem-key-001")).thenReturn(Optional.of(existing));

        AppointmentResponse response = appointmentService.bookAppointment(request);

        assertThat(response.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Book appointment throws on slot conflict")
    void bookAppointment_conflict_throws() {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(UUID.randomUUID());
        request.setDoctorId(UUID.randomUUID());
        request.setStartTime(LocalDateTime.now().plusDays(1));
        request.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        when(patientRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(buildPatient()));
        when(doctorRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(buildDoctor()));

        Appointment conflicting = Appointment.builder()
                .patient(buildPatient())
                .doctor(buildDoctor())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        when(appointmentRepository.findConflictingAppointments(any(), any(), any()))
                .thenReturn(List.of(conflicting));

        assertThatThrownBy(() -> appointmentService.bookAppointment(request))
                .isInstanceOf(SlotUnavailableException.class);
    }

    @Test
    @DisplayName("Cancel appointment sets status to CANCELLED")
    void cancelAppointment_success() {
        UUID id = UUID.randomUUID();
        Patient patient = buildPatient();
        Doctor doctor = buildDoctor();

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(appointmentRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        AppointmentResponse response = appointmentService.cancelAppointment(id, "Patient request");

        assertThat(response.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
    }

    @Test
    @DisplayName("Get appointment throws when not found")
    void getAppointment_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(appointmentRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.getAppointmentById(id))
                .isInstanceOf(AppointmentNotFoundException.class);
    }
}
