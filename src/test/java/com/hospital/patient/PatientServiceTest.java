package com.hospital.patient;

import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.PatientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock private PatientRepository patientRepository;

    private PatientService patientService;

    @BeforeEach
    void setUp() {
        patientService = new PatientService(patientRepository, new PatientMapperImpl());
    }

    @Test
    @DisplayName("Create patient generates MRN and saves")
    void createPatient_success() {
        PatientRequest request = new PatientRequest();
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setGender(Gender.FEMALE);
        request.setEmail("alice@example.com");

        when(patientRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(patientRepository.findMaxMrnSequence()).thenReturn(0);

        Patient savedPatient = Patient.builder()
                .firstName("Alice")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender(Gender.FEMALE)
                .email("alice@example.com")
                .mrn("MRN0000001")
                .build();

        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        PatientResponse response = patientService.createPatient(request);

        assertThat(response.getMrn()).isEqualTo("MRN0000001");
        assertThat(response.getFirstName()).isEqualTo("Alice");
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    @DisplayName("Create patient throws on duplicate email")
    void createPatient_duplicateEmail_throws() {
        PatientRequest request = new PatientRequest();
        request.setFirstName("Bob");
        request.setLastName("Jones");
        request.setDateOfBirth(LocalDate.of(1985, 5, 20));
        request.setGender(Gender.MALE);
        request.setEmail("existing@example.com");

        when(patientRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> patientService.createPatient(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("Get patient by ID returns response")
    void getPatientById_success() {
        UUID id = UUID.randomUUID();
        Patient patient = Patient.builder()
                .mrn("MRN0000001")
                .firstName("Alice")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender(Gender.FEMALE)
                .build();

        when(patientRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(patient));

        PatientResponse response = patientService.getPatientById(id);

        assertThat(response.getFirstName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("Get patient by ID throws when not found")
    void getPatientById_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(patientRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.getPatientById(id))
                .isInstanceOf(PatientNotFoundException.class);
    }

    @Test
    @DisplayName("Delete patient sets deletedAt")
    void deletePatient_softDeletes() {
        UUID id = UUID.randomUUID();
        Patient patient = Patient.builder()
                .mrn("MRN0000001")
                .firstName("Alice")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender(Gender.FEMALE)
                .build();

        when(patientRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        patientService.deletePatient(id);

        assertThat(patient.getDeletedAt()).isNotNull();
        verify(patientRepository).save(patient);
    }

    @Test
    @DisplayName("Get patient by MRN returns response")
    void getPatientByMrn_success() {
        Patient patient = Patient.builder()
                .mrn("MRN0000001")
                .firstName("Alice")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender(Gender.FEMALE)
                .build();

        when(patientRepository.findByMrnAndDeletedAtIsNull("MRN0000001")).thenReturn(Optional.of(patient));

        PatientResponse response = patientService.getPatientByMrn("MRN0000001");

        assertThat(response.getMrn()).isEqualTo("MRN0000001");
    }
}
