package com.hospital.ward;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BedRepository extends JpaRepository<Bed, UUID>, JpaSpecificationExecutor<Bed> {}
