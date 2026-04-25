package com.hospital.prescription;

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
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    @Query("SELECT p FROM Prescription p WHERE p.deletedAt IS NULL AND p.patient.id = :patientId ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByPatientId(@Param("patientId") UUID patientId);

    @Query("SELECT p FROM Prescription p WHERE p.deletedAt IS NULL AND p.doctor.id = :doctorId")
    Page<Prescription> findByDoctorId(@Param("doctorId") UUID doctorId, Pageable pageable);

    @Query("SELECT p FROM Prescription p WHERE p.deletedAt IS NULL AND p.status = 'ACTIVE'")
    List<Prescription> findActivePrescriptions();

    @Query("SELECT p FROM Prescription p WHERE p.deletedAt IS NULL")
    Page<Prescription> findAllActive(Pageable pageable);
}