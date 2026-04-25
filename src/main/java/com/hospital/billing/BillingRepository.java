package com.hospital.billing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillingRepository extends JpaRepository<Invoice, UUID> {

    Optional<Invoice> findByIdAndDeletedAtIsNull(UUID id);

    Optional<Invoice> findByIdempotencyKey(String idempotencyKey);

    Page<Invoice> findByPatientIdAndDeletedAtIsNull(UUID patientId, Pageable pageable);

    Page<Invoice> findByStatusAndDeletedAtIsNull(PaymentStatus status, Pageable pageable);

    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i WHERE i.invoiceDate BETWEEN :from AND :to AND i.deletedAt IS NULL")
    BigDecimal sumRevenueByDateRange(LocalDate from, LocalDate to);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(i.invoiceNumber, 4) AS int)), 0) FROM Invoice i")
    int findMaxInvoiceSequence();
}
