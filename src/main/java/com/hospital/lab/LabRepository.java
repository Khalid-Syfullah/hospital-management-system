package com.hospital.lab;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LabRepository extends JpaRepository<LabOrder, UUID> {

    Optional<LabOrder> findByIdAndDeletedAtIsNull(UUID id);

    Page<LabOrder> findByPatientIdAndDeletedAtIsNull(UUID patientId, Pageable pageable);

    Page<LabOrder> findByStatusAndDeletedAtIsNull(LabStatus status, Pageable pageable);
}
