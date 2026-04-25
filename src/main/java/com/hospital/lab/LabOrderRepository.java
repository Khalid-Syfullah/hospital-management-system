package com.hospital.lab;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LabOrderRepository extends JpaRepository<LabOrder, UUID>, JpaSpecificationExecutor<LabOrder> {}
