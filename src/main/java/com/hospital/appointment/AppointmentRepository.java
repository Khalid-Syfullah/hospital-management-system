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
    Page<Appointment> findByPatient(Patient patient, Pageable pageable);

    Page<Appointment> findByDoctor(Doctor doctor, Pageable pageable);

    Optional<Appointment> findByIdempotencyKey(String idempotencyKey);

    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor " +
            "AND a.appointmentDateTime BETWEEN :startTime AND :endTime " +
            "AND a.status != 'CANCELLED'")
    List<Appointment> findConflictingAppointments(
            @Param("doctor") Doctor doctor,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    Page<Appointment> findByStatus(Appointment.AppointmentStatus status, Pageable pageable);
}
