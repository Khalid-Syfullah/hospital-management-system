package com.hospital.lab;

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
public interface LabOrderRepository extends JpaRepository<LabOrder, UUID> {

    @Query("SELECT l FROM LabOrder l WHERE l.deletedAt IS NULL AND l.patient.id = :patientId ORDER BY l.orderDate DESC")
    List<LabOrder> findByPatientId(@Param("patientId") UUID patientId);

    @Query("SELECT l FROM LabOrder l WHERE l.deletedAt IS NULL AND l.status = :status")
    List<LabOrder> findByStatus(@Param("status") LabOrder.LabStatus status);

    @Query("SELECT l FROM LabOrder l WHERE l.deletedAt IS NULL")
    Page<LabOrder> findAllActive(Pageable pageable);
}