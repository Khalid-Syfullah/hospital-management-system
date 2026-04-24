package com.hospital.pharmacy;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, UUID> {
}
