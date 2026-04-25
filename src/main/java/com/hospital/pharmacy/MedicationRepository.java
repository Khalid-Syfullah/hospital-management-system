package com.hospital.pharmacy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, UUID> {

    Optional<Medication> findByIdAndDeletedAtIsNull(UUID id);

    Page<Medication> findByDeletedAtIsNull(Pageable pageable);

    @Query("SELECT m FROM Medication m WHERE m.stockQuantity <= m.reorderLevel AND m.deletedAt IS NULL AND m.active = true")
    List<Medication> findLowStockMedications();
}
