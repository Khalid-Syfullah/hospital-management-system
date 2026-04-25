package com.hospital.medicalrecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
@org.springframework.stereotype.Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {}
