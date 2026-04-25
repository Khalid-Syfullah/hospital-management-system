package com.hospital.department;

import com.hospital.audit.AuditService;
import com.hospital.doctor.DoctorRepository;
import com.hospital.exception.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentService {
    private final DepartmentRepository repository;
    private final DoctorRepository doctors;
    private final AuditService audit;

    public DepartmentService(DepartmentRepository repository, DoctorRepository doctors, AuditService audit) {
        this.repository = repository;
        this.doctors = doctors;
        this.audit = audit;
    }

    @Cacheable("departments")
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(DepartmentResponse::from);
    }

    @Transactional(readOnly = true)
    public DepartmentResponse get(UUID id) {
        return DepartmentResponse.from(find(id));
    }

    @CacheEvict(value = "departments", allEntries = true)
    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        Department department = new Department();
        apply(department, request);
        repository.save(department);
        audit.record("Department", department.getId().toString(), "CREATE", "created");
        return DepartmentResponse.from(department);
    }

    @CacheEvict(value = "departments", allEntries = true)
    @Transactional
    public DepartmentResponse update(UUID id, DepartmentRequest request) {
        Department department = find(id);
        apply(department, request);
        audit.record("Department", id.toString(), "UPDATE", "updated");
        return DepartmentResponse.from(department);
    }

    private void apply(Department department, DepartmentRequest request) {
        department.setName(request.name());
        department.setDescription(request.description());
        department.setHeadDoctor(request.headDoctorId() == null ? null :
                doctors.findById(request.headDoctorId()).orElseThrow(() -> new ResourceNotFoundException("Doctor", request.headDoctorId())));
    }

    private Department find(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department", id));
    }
}
