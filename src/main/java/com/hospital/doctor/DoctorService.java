package com.hospital.doctor;

import com.hospital.audit.AuditService;
import com.hospital.department.DepartmentRepository;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.user.UserRepository;
import java.util.HashSet;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorService {
    private final DoctorRepository repository;
    private final DepartmentRepository departments;
    private final UserRepository users;
    private final AuditService audit;
    public DoctorService(DoctorRepository repository, DepartmentRepository departments, UserRepository users, AuditService audit) {
        this.repository = repository; this.departments = departments; this.users = users; this.audit = audit;
    }
    @Transactional(readOnly = true) public Page<DoctorResponse> list(Pageable pageable) { return repository.findAll(pageable).map(DoctorResponse::from); }
    @Transactional(readOnly = true) public DoctorResponse get(UUID id) { return DoctorResponse.from(find(id)); }
    @CacheEvict(value = "doctorAvailability", allEntries = true)
    @Transactional public DoctorResponse create(DoctorRequest request) {
        Doctor doctor = new Doctor(); apply(doctor, request); repository.save(doctor);
        audit.record("Doctor", doctor.getId().toString(), "CREATE", "created"); return DoctorResponse.from(doctor);
    }
    @CacheEvict(value = "doctorAvailability", allEntries = true)
    @Transactional public DoctorResponse update(UUID id, DoctorRequest request) {
        Doctor doctor = find(id); apply(doctor, request); audit.record("Doctor", id.toString(), "UPDATE", "updated"); return DoctorResponse.from(doctor);
    }
    @Cacheable("doctorAvailability")
    @Transactional(readOnly = true) public String availability(UUID id) { return find(id).getAvailability(); }
    private void apply(Doctor d, DoctorRequest r) {
        d.setFullName(r.fullName()); d.setLicenseNumber(r.licenseNumber()); d.setCredentials(r.credentials());
        d.setSpecializations(r.specializations() == null ? new HashSet<>() : new HashSet<>(r.specializations()));
        d.setAvailability(r.availability());
        d.setDepartment(r.departmentId() == null ? null : departments.findById(r.departmentId()).orElseThrow(() -> new ResourceNotFoundException("Department", r.departmentId())));
        d.setUser(r.userId() == null ? null : users.findById(r.userId()).orElseThrow(() -> new ResourceNotFoundException("User", r.userId())));
    }
    public Doctor find(UUID id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor", id)); }
}
