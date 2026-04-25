package com.hospital.patient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PatientServiceTests {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    public void setup() {
        patientRepository.deleteAll();
    }

    @Test
    public void testCreatePatient_Success() {
        PatientRequest request = PatientRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender("M")
                .phoneNumber("1234567890")
                .email("john@example.com")
                .bloodType("O+")
                .build();

        PatientResponse response = patientService.createPatient(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertNotNull(response.getMrn());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
    }

    @Test
    public void testGetPatient_Success() {
        PatientRequest request = PatientRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .gender("F")
                .phoneNumber("9876543210")
                .email("jane@example.com")
                .build();

        PatientResponse created = patientService.createPatient(request);
        PatientResponse retrieved = patientService.getPatient(created.getId());

        assertNotNull(retrieved);
        assertEquals(created.getId(), retrieved.getId());
        assertEquals("Jane", retrieved.getFirstName());
    }

    @Test
    public void testGetPatientByMrn_Success() {
        PatientRequest request = PatientRequest.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .dateOfBirth(LocalDate.of(1992, 3, 20))
                .gender("M")
                .phoneNumber("5555555555")
                .email("bob@example.com")
                .build();

        PatientResponse created = patientService.createPatient(request);
        PatientResponse retrieved = patientService.getPatientByMrn(created.getMrn());

        assertNotNull(retrieved);
        assertEquals(created.getMrn(), retrieved.getMrn());
    }
}
