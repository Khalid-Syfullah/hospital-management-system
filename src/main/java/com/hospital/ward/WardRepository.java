package com.hospital.ward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WardRepository extends JpaRepository<Ward, UUID> {
    Optional<Ward> findByIdAndDeletedAtIsNull(UUID id);
    boolean existsByName(String name);
}
