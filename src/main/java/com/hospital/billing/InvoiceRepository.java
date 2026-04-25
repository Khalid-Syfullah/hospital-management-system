package com.hospital.billing;

import com.hospital.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    @Query("SELECT i FROM Invoice i WHERE i.deletedAt IS NULL AND i.patient.id = :patientId ORDER BY i.invoiceDate DESC")
    List<Invoice> findByPatientId(@Param("patientId") UUID patientId);

    @Query("SELECT i FROM Invoice i WHERE i.deletedAt IS NULL AND i.status = :status")
    Page<Invoice> findByStatus(@Param("status") Invoice.PaymentStatus status, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE i.deletedAt IS NULL")
    Page<Invoice> findAllActive(Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE i.idempotencyKey = :key")
    Optional<Invoice> findByIdempotencyKey(@Param("key") String key);
}