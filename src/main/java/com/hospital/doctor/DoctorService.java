package com.hospital.doctor;

import com.hospital.department.Department;
import com.hospital.department.DepartmentRepository;
import com.hospital.doctor.DoctorDtos.DoctorRequest;
import com.hospital.doctor.DoctorDtos.DoctorResponse;
import com.hospital.exception.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorMapper doctorMapper;

    public DoctorService(DoctorRepository doctorRepository, DepartmentRepository departmentRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.doctorMapper = doctorMapper;
    }

    @Transactional
    @CacheEvict(value = "doctorAvailability", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    public DoctorResponse create(DoctorRequest request) {
        Department department = resolveDepartment(request.departmentId());
        Doctor doctor = new Doctor(request.fullName(), request.licenseNumber(), request.specialization(), request.availability());
        doctor.update(request.fullName(), request.licenseNumber(), request.specialization(), request.availability(), department);
        return doctorMapper.toResponse(doctorRepository.save(doctor));
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponse> list(Pageable pageable) {
        return doctorRepository.findAll(pageable).map(doctorMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "doctorAvailability", key = "#id")
    public DoctorResponse get(UUID id) {
        return doctorMapper.toResponse(doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found")));
    }

    private Department resolveDepartment(UUID id) {
        return id == null ? null : departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
    }
}
