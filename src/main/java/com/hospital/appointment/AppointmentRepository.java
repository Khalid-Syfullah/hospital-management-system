package com.hospital.appointment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID>, JpaSpecificationExecutor<Appointment> {

    Optional<Appointment> findByIdempotencyKey(String idempotencyKey);

    Optional<Appointment> findByIdAndDeletedAtIsNull(UUID id);

    Page<Appointment> findByPatientIdAndDeletedAtIsNull(UUID patientId, Pageable pageable);

    Page<Appointment> findByDoctorIdAndDeletedAtIsNull(UUID doctorId, Pageable pageable);

    @Query("""
            SELECT a FROM Appointment a
            WHERE a.doctor.id = :doctorId
            AND a.status NOT IN ('CANCELLED', 'NO_SHOW')
            AND a.deletedAt IS NULL
            AND ((a.startTime < :endTime AND a.endTime > :startTime))
            """)
    List<Appointment> findConflictingAppointments(UUID doctorId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("""
            SELECT a FROM Appointment a
            WHERE a.status = 'SCHEDULED'
            AND a.startTime BETWEEN :from AND :to
            AND a.deletedAt IS NULL
            """)
    List<Appointment> findUpcomingAppointments(LocalDateTime from, LocalDateTime to);
}
