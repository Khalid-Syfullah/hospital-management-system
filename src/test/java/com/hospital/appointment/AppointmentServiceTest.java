package com.hospital.appointment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hospital.audit.AuditService;
import com.hospital.doctor.Doctor;
import com.hospital.doctor.DoctorService;
import com.hospital.exception.SlotUnavailableException;
import com.hospital.notification.NotificationService;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientService;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AppointmentServiceTest {
    @Test
    void bookRejectsConflictingSlot() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        UUID doctorId = UUID.randomUUID();
        OffsetDateTime start = OffsetDateTime.now().plusDays(1);
        OffsetDateTime end = start.plusMinutes(30);
        when(repository.findByIdempotencyKey("k1")).thenReturn(Optional.empty());
        when(repository.hasConflict(doctorId, start, end)).thenReturn(true);
        AppointmentService service = new AppointmentService(repository, mock(PatientService.class), mock(DoctorService.class),
                mock(NotificationService.class), mock(AuditService.class));

        assertThatThrownBy(() -> service.book(new AppointmentRequest(UUID.randomUUID(), doctorId, start, end, "Checkup"), "k1"))
                .isInstanceOf(SlotUnavailableException.class);
    }

    @Test
    void bookReturnsExistingForSameIdempotencyKey() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID());
        Doctor doctor = new Doctor();
        doctor.setId(UUID.randomUUID());
        Appointment appointment = new Appointment();
        appointment.setId(UUID.randomUUID());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setStartTime(OffsetDateTime.now().plusDays(1));
        appointment.setEndTime(OffsetDateTime.now().plusDays(1).plusMinutes(30));
        appointment.setIdempotencyKey("same");
        when(repository.findByIdempotencyKey("same")).thenReturn(Optional.of(appointment));
        AppointmentService service = new AppointmentService(repository, mock(PatientService.class), mock(DoctorService.class),
                mock(NotificationService.class), mock(AuditService.class));

        AppointmentResponse response = service.book(new AppointmentRequest(patient.getId(), doctor.getId(),
                appointment.getStartTime(), appointment.getEndTime(), "Checkup"), "same");

        assertThat(response.id()).isEqualTo(appointment.getId());
    }
}
