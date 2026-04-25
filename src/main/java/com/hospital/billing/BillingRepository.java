package com.hospital.billing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
@org.springframework.stereotype.Repository
public interface BillingRepository extends JpaRepository<Billing, UUID> {
    Optional<Billing> findByIdempotencyKey(String key);
}
