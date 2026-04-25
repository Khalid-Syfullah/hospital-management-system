package com.hospital.ward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BedRepository extends JpaRepository<Bed, UUID> {
    Optional<Bed> findByIdAndDeletedAtIsNull(UUID id);
    List<Bed> findByWardIdAndStatus(UUID wardId, BedStatus status);
    long countByWardIdAndStatus(UUID wardId, BedStatus status);
}
