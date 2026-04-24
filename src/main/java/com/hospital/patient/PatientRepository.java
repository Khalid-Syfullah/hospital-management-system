package com.hospital.patient;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByMrnAndDeletedAtIsNull(String mrn);

    Page<Patient> findByDeletedAtIsNull(Pageable pageable);
}
