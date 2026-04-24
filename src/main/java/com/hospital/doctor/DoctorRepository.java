package com.hospital.doctor;

import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    @Override
    @EntityGraph(attributePaths = "department")
    Optional<Doctor> findById(UUID id);
}
