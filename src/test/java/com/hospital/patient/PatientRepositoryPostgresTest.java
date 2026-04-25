package com.hospital.patient;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers(disabledWithoutDocker = true)
class PatientRepositoryPostgresTest {
    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.flyway.enabled", () -> "false");
    }

    @Autowired PatientRepository repository;

    @Test
    void persistsAndFindsPatientByMrnOnPostgres() {
        Patient patient = new Patient();
        patient.setMrn("MRN-PG-000001");
        patient.setFullName("Postgres Patient");
        patient.setGender(Patient.Gender.UNKNOWN);
        patient.setCreatedAt(Instant.now());
        patient.setUpdatedAt(Instant.now());

        repository.saveAndFlush(patient);

        assertThat(repository.findByMrn("MRN-PG-000001")).isPresent();
    }
}
