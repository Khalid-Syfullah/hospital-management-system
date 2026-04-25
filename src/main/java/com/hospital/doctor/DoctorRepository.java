package com.hospital.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Optional<Doctor> findByUserId(UUID userId);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    @Query("SELECT d FROM Doctor d WHERE d.deletedAt IS NULL")
    Page<Doctor> findAllActive(Pageable pageable);

    @Query("SELECT d FROM Doctor d WHERE d.deletedAt IS NULL AND d.department.id = :departmentId")
    List<Doctor> findByDepartmentId(@Param("departmentId") UUID departmentId);

    @Query("SELECT d FROM Doctor d WHERE d.deletedAt IS NULL AND d.specialization LIKE %:specialization%")
    Page<Doctor> findBySpecialization(@Param("specialization") String specialization, Pageable pageable);

    @Query("SELECT d FROM Doctor d WHERE d.deletedAt IS NULL AND " +
           "(LOWER(d.user.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.user.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Doctor> searchDoctors(@Param("keyword") String keyword, Pageable pageable);
}