package com.hospital.billing;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, UUID> {}
