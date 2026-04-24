package com.hospital.lab;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabOrderRepository extends JpaRepository<LabOrder, UUID> {
}
