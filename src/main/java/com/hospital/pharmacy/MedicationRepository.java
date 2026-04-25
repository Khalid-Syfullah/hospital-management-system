package com.hospital.pharmacy;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MedicationRepository extends JpaRepository<Medication, UUID>, JpaSpecificationExecutor<Medication> {}
