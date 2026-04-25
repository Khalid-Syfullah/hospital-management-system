package com.hospital.department;

import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new BadRequestException("Department with name already exists");
        }
        Department department = departmentMapper.toEntity(request);
        department.setActive(true);
        department.setCreatedBy("system");
        department.setUpdatedBy("system");
        department = departmentRepository.save(department);
        log.info("Department created: {}", department.getName());
        return departmentMapper.toResponse(department);
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartment(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        return departmentMapper.toResponse(department);
    }

    @Transactional
    public DepartmentResponse updateDepartment(UUID id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setPhoneNumber(request.getPhoneNumber());
        department.setHeadOfDepartment(request.getHeadOfDepartment());
        department.setUpdatedBy("system");
        department = departmentRepository.save(department);
        log.info("Department updated: {}", department.getName());
        return departmentMapper.toResponse(department);
    }

    @Transactional
    public void deleteDepartment(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        department.setActive(false);
        departmentRepository.save(department);
        log.info("Department deactivated: {}", department.getName());
    }

    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getAllDepartments(Pageable pageable) {
        Page<Department> departments = departmentRepository.findAll(pageable);
        List<DepartmentResponse> responses = departments.getContent().stream()
                .map(departmentMapper::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, pageable, departments.getTotalElements());
    }
}
