package com.hospital.department;

import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    @CacheEvict(value = "departments", allEntries = true)
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Department already exists: " + request.getName());
        }
        return departmentMapper.toResponse(departmentRepository.save(departmentMapper.toEntity(request)));
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(UUID id) {
        return departmentMapper.toResponse(findActiveDepartment(id));
    }

    @Transactional(readOnly = true)
    @Cacheable("departments")
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .filter(d -> !d.isDeleted())
                .map(departmentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getPagedDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable).map(departmentMapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "departments", allEntries = true)
    public DepartmentResponse updateDepartment(UUID id, DepartmentRequest request) {
        Department department = findActiveDepartment(id);
        departmentMapper.updateEntity(request, department);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Transactional
    @CacheEvict(value = "departments", allEntries = true)
    public void deleteDepartment(UUID id) {
        Department department = findActiveDepartment(id);
        department.softDelete();
        departmentRepository.save(department);
    }

    private Department findActiveDepartment(UUID id) {
        return departmentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
    }
}
