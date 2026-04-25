package com.hospital.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID>, JpaSpecificationExecutor<Doctor> {

    Optional<Doctor> findByIdAndDeletedAtIsNull(UUID id);

    Optional<Doctor> findByUserIdAndDeletedAtIsNull(UUID userId);

    @Query("SELECT d FROM Doctor d LEFT JOIN FETCH d.availabilitySlots WHERE d.id = :id AND d.deletedAt IS NULL")
    Optional<Doctor> findByIdWithAvailability(UUID id);
}
