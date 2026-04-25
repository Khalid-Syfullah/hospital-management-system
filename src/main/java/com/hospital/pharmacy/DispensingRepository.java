package com.hospital.pharmacy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DispensingRepository extends JpaRepository<Dispensing, UUID> {
}
