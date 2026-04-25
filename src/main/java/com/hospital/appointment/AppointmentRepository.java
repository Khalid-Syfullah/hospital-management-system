package com.hospital.appointment;

import com.hospital.doctor.Doctor;
import com.hospital.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("SELECT a FROM Appointment a WHERE a.deletedAt IS NULL")
    Page<Appointment> findAllActive(Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.deletedAt IS NULL AND a.patient.id = :patientId")
    List<Appointment> findByPatientId(@Param("patientId") UUID patientId);

    @Query("SELECT a FROM Appointment a WHERE a.deletedAt IS NULL AND a.doctor.id = :doctorId")
    List<Appointment> findByDoctorId(@Param("doctorId") UUID doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.deletedAt IS NULL AND a.doctor.id = :doctorId AND a.appointmentDate >= :startDate AND a.appointmentDate < :endDate AND a.status NOT IN ('CANCELLED')")
    List<Appointment> findByDoctorIdAndDateRange(@Param("doctorId") UUID doctorId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM Appointment a WHERE a.idempotencyKey = :key")
    Optional<Appointment> findByIdempotencyKey(@Param("key") String key);

    @Query("SELECT a FROM Appointment a WHERE a.deletedAt IS NULL AND a.appointmentDate >= :start AND a.appointmentDate < :end")
    Page<Appointment> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);
}