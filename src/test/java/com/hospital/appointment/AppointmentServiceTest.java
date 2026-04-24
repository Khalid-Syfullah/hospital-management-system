package com.hospital.appointment;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.hospital.audit.AuditService;
import com.hospital.doctor.Doctor;
import com.hospital.doctor.DoctorRepository;
import com.hospital.exception.SlotUnavailableException;
import com.hospital.notification.NotificationService;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientService;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {
    @Mock
    AppointmentRepository appointmentRepository;
    @Mock
    PatientService patientService;
    @Mock
    DoctorRepository doctorRepository;
    @Mock
    NotificationService notificationService;
    @Mock
    AuditService auditService;

    @Test
    void bookingRejectsOverlappingDoctorSlot() {
        UUID patientId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusMinutes(30);
        when(appointmentRepository.existsOverlappingAppointment(eq(doctorId), eq(start), eq(end))).thenReturn(true);
        AppointmentService service = new AppointmentService(appointmentRepository, patientService, doctorRepository,
                Mappers.getMapper(AppointmentMapper.class), notificationService, auditService);

        assertThatThrownBy(() -> service.book(new AppointmentDtos.BookAppointmentRequest(patientId, doctorId, start, end), "idem-1"))
                .isInstanceOf(SlotUnavailableException.class);
    }

    @Test
    void bookingReturnsExistingAppointmentForSameIdempotencyKey() {
        Patient patient = new Patient("MRN-1", "Patient", null, null);
        Doctor doctor = new Doctor("Doctor", "LIC-1", "Cardiology", "Weekdays");
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        Appointment existing = new Appointment(patient, doctor, start, start.plusMinutes(30), "idem-1");
        when(appointmentRepository.findByIdempotencyKey("idem-1")).thenReturn(Optional.of(existing));
        AppointmentService service = new AppointmentService(appointmentRepository, patientService, doctorRepository,
                Mappers.getMapper(AppointmentMapper.class), notificationService, auditService);

        var response = service.book(new AppointmentDtos.BookAppointmentRequest(UUID.randomUUID(), UUID.randomUUID(), start, start.plusMinutes(30)), "idem-1");

        org.assertj.core.api.Assertions.assertThat(response.status()).isEqualTo(AppointmentStatus.SCHEDULED);
    }
}
