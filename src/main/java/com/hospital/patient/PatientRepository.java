package com.hospital.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByMrn(String mrn);

    Optional<Patient> findByUserId(UUID userId);

    boolean existsByMrn(String mrn);

    @Query("SELECT p FROM Patient p WHERE p.deletedAt IS NULL")
    Page<Patient> findAllActive(Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE p.deletedAt IS NULL AND " +
           "(LOWER(p.mrn) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.user.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.user.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Patient> searchPatients(@Param("keyword") String keyword, Pageable pageable);
}