package com.hospital.appointment;

import com.hospital.doctor.Doctor;
import com.hospital.doctor.DoctorRepository;
import com.hospital.department.Department;
import com.hospital.department.DepartmentRepository;
import com.hospital.exception.ConflictException;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AppointmentDoubleBookingTests {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Patient testPatient;
    private Doctor testDoctor;

    @BeforeEach
    public void setup() {
        appointmentRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
        departmentRepository.deleteAll();

        // Create test department
        Department dept = Department.builder()
                .name("Test Dept")
                .active(true)
                .createdBy("system")
                .updatedBy("system")
                .build();
        dept = departmentRepository.save(dept);

        // Create test patient
        testPatient = Patient.builder()
                .firstName("Test")
                .lastName("Patient")
                .mrn("MRN-TEST-001")
                .phoneNumber("1234567890")
                .active(true)
                .createdBy("system")
                .updatedBy("system")
                .build();
        testPatient = patientRepository.save(testPatient);

        // Create test doctor
        testDoctor = Doctor.builder()
                .firstName("Test")
                .lastName("Doctor")
                .phoneNumber("9876543210")
                .department(dept)
                .active(true)
                .available(true)
                .createdBy("system")
                .updatedBy("system")
                .build();
        testDoctor = doctorRepository.save(testDoctor);
    }

    @Test
    public void testDoubleBookingPrevention() {
        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);

        AppointmentRequest request1 = AppointmentRequest.builder()
                .patientId(testPatient.getId())
                .doctorId(testDoctor.getId())
                .appointmentDateTime(appointmentTime)
                .durationMinutes(30)
                .reason("Checkup")
                .idempotencyKey("key1")
                .build();

        // First appointment should succeed
        AppointmentResponse response1 = appointmentService.bookAppointment(request1);
        assertNotNull(response1);

        // Second appointment at same time should fail
        AppointmentRequest request2 = AppointmentRequest.builder()
                .patientId(testPatient.getId())
                .doctorId(testDoctor.getId())
                .appointmentDateTime(appointmentTime)
                .durationMinutes(30)
                .reason("Another checkup")
                .idempotencyKey("key2")
                .build();

        assertThrows(ConflictException.class, () -> appointmentService.bookAppointment(request2));
    }

    @Test
    public void testIdempotency() {
        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(2).withHour(14).withMinute(0).withSecond(0).withNano(0);
        String idempotencyKey = "idempotent-key-123";

        AppointmentRequest request = AppointmentRequest.builder()
                .patientId(testPatient.getId())
                .doctorId(testDoctor.getId())
                .appointmentDateTime(appointmentTime)
                .durationMinutes(30)
                .reason("Checkup")
                .idempotencyKey(idempotencyKey)
                .build();

        // First call
        AppointmentResponse response1 = appointmentService.bookAppointment(request);

        // Second call with same idempotency key
        AppointmentResponse response2 = appointmentService.bookAppointment(request);

        // Both should return same appointment
        assertEquals(response1.getId(), response2.getId());
    }
}
