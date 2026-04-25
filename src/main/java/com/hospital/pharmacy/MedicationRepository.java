package com.hospital.pharmacy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, UUID> {

    Optional<Medication> findByName(String name);

    Optional<Medication> findByCode(String code);

    @Query("SELECT m FROM Medication m WHERE m.deletedAt IS NULL AND m.isActive = true AND m.currentStock <= m.reorderLevel")
    List<Medication> findLowStock();

    @Query("SELECT m FROM Medication m WHERE m.deletedAt IS NULL AND m.expiryDate <= :expiryDate")
    List<Medication> findExpiringSoon(@Param("expiryDate") LocalDate expiryDate);

    @Query("SELECT m FROM Medication m WHERE m.deletedAt IS NULL AND m.isActive = true")
    Page<Medication> findAllActive(Pageable pageable);

    @Query("SELECT m FROM Medication m WHERE m.deletedAt IS NULL AND LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Medication> search(@Param("keyword") String keyword, Pageable pageable);
}