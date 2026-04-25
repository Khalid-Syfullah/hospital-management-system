package com.hospital.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID>, JpaSpecificationExecutor<Patient> {

    Optional<Patient> findByMrnAndDeletedAtIsNull(String mrn);

    Optional<Patient> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByEmail(String email);

    boolean existsByMrn(String mrn);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(p.mrn, 4) AS int)), 0) FROM Patient p")
    int findMaxMrnSequence();
}
