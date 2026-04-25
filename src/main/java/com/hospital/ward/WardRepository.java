package com.hospital.ward;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WardRepository extends JpaRepository<Ward, UUID> {

    @Query("SELECT w FROM Ward w WHERE w.deletedAt IS NULL AND w.isActive = true")
    Page<Ward> findAllActive(Pageable pageable);
}