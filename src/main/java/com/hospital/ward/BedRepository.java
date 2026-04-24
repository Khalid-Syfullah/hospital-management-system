package com.hospital.ward;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BedRepository extends JpaRepository<Bed, UUID> {
}
