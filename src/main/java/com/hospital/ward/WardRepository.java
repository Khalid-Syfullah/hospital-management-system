package com.hospital.ward;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WardRepository extends JpaRepository<Ward, UUID>, JpaSpecificationExecutor<Ward> {}
