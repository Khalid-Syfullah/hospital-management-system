package com.hospital.ward;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
@org.springframework.stereotype.Repository
public interface WardRepository extends JpaRepository<Ward, UUID> {
    Optional<Ward> findByWardName(String name);
}
