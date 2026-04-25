package com.hospital.lab;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
@org.springframework.stereotype.Repository
public interface LabRepository extends JpaRepository<Lab, UUID> {}
