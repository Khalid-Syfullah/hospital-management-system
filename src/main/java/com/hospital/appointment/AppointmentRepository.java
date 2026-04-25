package com.hospital.appointment;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID>, JpaSpecificationExecutor<Appointment> {
    Optional<Appointment> findByIdempotencyKey(String idempotencyKey);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select count(a) > 0 from Appointment a
            where a.doctor.id = :doctorId
              and a.status <> com.hospital.appointment.Appointment.Status.CANCELLED
              and a.startTime < :endTime
              and a.endTime > :startTime
            """)
    boolean hasConflict(@Param("doctorId") UUID doctorId, @Param("startTime") OffsetDateTime startTime, @Param("endTime") OffsetDateTime endTime);
}
