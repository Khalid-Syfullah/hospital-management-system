package com.hospital.medicalrecord;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {

    Optional<MedicalRecord> findByIdAndDeletedAtIsNull(UUID id);

    Page<MedicalRecord> findByPatientIdAndDeletedAtIsNull(UUID patientId, Pageable pageable);

    Page<MedicalRecord> findByDoctorIdAndDeletedAtIsNull(UUID doctorId, Pageable pageable);
}
