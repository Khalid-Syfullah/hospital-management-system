package com.hospital.department;

import com.hospital.department.DepartmentDtos.DepartmentRequest;
import com.hospital.department.DepartmentDtos.DepartmentResponse;
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
public class DepartmentService {
    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;

    public DepartmentService(DepartmentRepository repository, DepartmentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @CacheEvict(value = "departments", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    public DepartmentResponse create(DepartmentRequest request) {
        return mapper.toResponse(repository.save(new Department(request.name(), request.description(), request.headOfDepartment())));
    }

    @Transactional(readOnly = true)
    @Cacheable("departments")
    public Page<DepartmentResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "departments", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    public DepartmentResponse update(UUID id, DepartmentRequest request) {
        Department department = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        department.update(request.name(), request.description(), request.headOfDepartment());
        return mapper.toResponse(department);
    }
}
