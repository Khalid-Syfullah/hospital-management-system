package com.hospital.doctor;

import com.hospital.department.Department;
import com.hospital.department.DepartmentRepository;
import com.hospital.exception.DoctorNotFoundException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.user.User;
import com.hospital.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorMapper doctorMapper;

    @Transactional
    @CacheEvict(value = "doctorProfiles", allEntries = true)
    public DoctorResponse createDoctor(DoctorRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));

        Doctor doctor = Doctor.builder()
                .user(user)
                .specialization(request.getSpecialization())
                .licenseNumber(request.getLicenseNumber())
                .qualification(request.getQualification())
                .yearsOfExperience(request.getYearsOfExperience())
                .biography(request.getBiography())
                .consultationFee(request.getConsultationFee())
                .build();

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findByIdAndDeletedAtIsNull(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", request.getDepartmentId()));
            doctor.setDepartment(dept);
        }

        return doctorMapper.toResponse(doctorRepository.save(doctor));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "doctorProfiles", key = "#id")
    public DoctorResponse getDoctorById(UUID id) {
        return doctorMapper.toResponse(findActiveDoctor(id));
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponse> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable).map(doctorMapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "doctorProfiles", key = "#id")
    public DoctorResponse updateDoctor(UUID id, DoctorRequest request) {
        Doctor doctor = findActiveDoctor(id);
        doctorMapper.updateEntity(request, doctor);

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findByIdAndDeletedAtIsNull(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", request.getDepartmentId()));
            doctor.setDepartment(dept);
        }

        return doctorMapper.toResponse(doctorRepository.save(doctor));
    }

    @Transactional
    @CacheEvict(value = "doctorProfiles", allEntries = true)
    public void deleteDoctor(UUID id) {
        Doctor doctor = findActiveDoctor(id);
        doctor.softDelete();
        doctorRepository.save(doctor);
    }

    private Doctor findActiveDoctor(UUID id) {
        return doctorRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }
}
