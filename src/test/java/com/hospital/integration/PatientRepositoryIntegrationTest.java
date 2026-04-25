package com.hospital.integration;

import com.hospital.patient.Gender;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.hospital.common.JpaAuditingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
class PatientRepositoryIntegrationTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    @DisplayName("Save and find patient by MRN")
    void saveAndFindByMrn() {
        Patient patient = Patient.builder()
                .mrn("MRN0000001")
                .firstName("Alice")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender(Gender.FEMALE)
                .email("alice@example.com")
                .build();

        patientRepository.save(patient);

        Optional<Patient> found = patientRepository.findByMrnAndDeletedAtIsNull("MRN0000001");

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Alice");
        assertThat(found.get().getMrn()).isEqualTo("MRN0000001");
    }

    @Test
    @DisplayName("Soft deleted patient not returned by findByMrnAndDeletedAtIsNull")
    void softDeletedPatient_notFoundByMrn() {
        Patient patient = Patient.builder()
                .mrn("MRN0000002")
                .firstName("Bob")
                .lastName("Jones")
                .dateOfBirth(LocalDate.of(1985, 5, 20))
                .gender(Gender.MALE)
                .build();

        patient.softDelete();
        patientRepository.save(patient);

        Optional<Patient> found = patientRepository.findByMrnAndDeletedAtIsNull("MRN0000002");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("existsByEmail returns true for existing email")
    void existsByEmail_true() {
        Patient patient = Patient.builder()
                .mrn("MRN0000003")
                .firstName("Carol")
                .lastName("White")
                .dateOfBirth(LocalDate.of(1995, 3, 15))
                .gender(Gender.FEMALE)
                .email("carol@example.com")
                .build();

        patientRepository.save(patient);

        assertThat(patientRepository.existsByEmail("carol@example.com")).isTrue();
        assertThat(patientRepository.existsByEmail("nonexistent@example.com")).isFalse();
    }

    @Test
    @DisplayName("findMaxMrnSequence returns 0 when no patients")
    void findMaxMrnSequence_empty() {
        int max = patientRepository.findMaxMrnSequence();
        assertThat(max).isEqualTo(0);
    }
}
