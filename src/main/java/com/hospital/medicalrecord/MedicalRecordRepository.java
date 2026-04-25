package com.hospital.medicalrecord;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID>, JpaSpecificationExecutor<MedicalRecord> {}
