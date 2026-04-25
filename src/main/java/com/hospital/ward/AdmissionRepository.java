package com.hospital.ward;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, UUID> {
    Optional<Admission> findByPatientIdAndActiveTrue(UUID patientId);
    Page<Admission> findByPatientId(UUID patientId, Pageable pageable);
    Optional<Admission> findByBedIdAndActiveTrue(UUID bedId);
}
