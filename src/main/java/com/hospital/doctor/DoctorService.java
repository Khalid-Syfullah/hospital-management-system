package com.hospital.doctor;

import com.hospital.department.Department;
import com.hospital.department.DepartmentRepository;
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
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorMapper doctorMapper;

    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Doctor doctor = doctorMapper.toEntity(request);
        doctor.setDepartment(department);
        doctor.setActive(true);
        doctor.setAvailable(true);
        doctor.setCreatedBy("system");
        doctor.setUpdatedBy("system");
        doctor = doctorRepository.save(doctor);
        log.info("Doctor created: {} {}", doctor.getFirstName(), doctor.getLastName());
        return doctorMapper.toResponse(doctor);
    }

    @Transactional(readOnly = true)
    public DoctorResponse getDoctor(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return doctorMapper.toResponse(doctor);
    }

    @Transactional
    public DoctorResponse updateDoctor(UUID id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            doctor.setDepartment(department);
        }

        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setEmail(request.getEmail());
        doctor.setAvailability(request.getAvailability());
        doctor.setUpdatedBy("system");
        doctor = doctorRepository.save(doctor);
        log.info("Doctor updated: {} {}", doctor.getFirstName(), doctor.getLastName());
        return doctorMapper.toResponse(doctor);
    }

    @Transactional
    public void deleteDoctor(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        doctor.setActive(false);
        doctorRepository.save(doctor);
        log.info("Doctor deactivated: {} {}", doctor.getFirstName(), doctor.getLastName());
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponse> getAllDoctors(Pageable pageable) {
        Page<Doctor> doctors = doctorRepository.findAll(pageable);
        List<DoctorResponse> responses = doctors.getContent().stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, pageable, doctors.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponse> searchDoctors(String keyword, Pageable pageable) {
        Page<Doctor> doctors = doctorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword, pageable);
        List<DoctorResponse> responses = doctors.getContent().stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, pageable, doctors.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponse> getDoctorsByDepartment(UUID departmentId, Pageable pageable) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Page<Doctor> doctors = doctorRepository.findByDepartment(department, pageable);
        List<DoctorResponse> responses = doctors.getContent().stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, pageable, doctors.getTotalElements());
    }
}
