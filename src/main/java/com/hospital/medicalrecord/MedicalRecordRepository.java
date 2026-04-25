package com.hospital.medicalrecord;

import com.hospital.appointment.Appointment;
import com.hospital.doctor.Doctor;
import com.hospital.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {

    @Query("SELECT m FROM MedicalRecord m WHERE m.deletedAt IS NULL AND m.patient.id = :patientId ORDER BY m.visitDate DESC")
    List<MedicalRecord> findByPatientId(@Param("patientId") UUID patientId);

    @Query("SELECT m FROM MedicalRecord m WHERE m.deletedAt IS NULL AND m.doctor.id = :doctorId")
    Page<MedicalRecord> findByDoctorId(@Param("doctorId") UUID doctorId, Pageable pageable);

    @Query("SELECT m FROM MedicalRecord m WHERE m.deletedAt IS NULL")
    Page<MedicalRecord> findAllActive(Pageable pageable);

    @Query("SELECT m FROM MedicalRecord m WHERE m.deletedAt IS NULL AND m.patient.id = :patientId AND m.icd10Code = :icd10Code")
    List<MedicalRecord> findByPatientIdAndIcd10Code(@Param("patientId") UUID patientId, @Param("icd10Code") String icd10Code);
}