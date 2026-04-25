package com.hospital.prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
@org.springframework.stereotype.Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {}
