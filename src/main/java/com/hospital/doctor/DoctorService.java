package com.hospital.doctor;

import com.hospital.department.Department;
import com.hospital.department.DepartmentRepository;
import com.hospital.exception.BadRequestException;
import com.hospital.user.Role;
import com.hospital.user.User;
import com.hospital.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

import com.hospital.department.Department;
import com.hospital.department.DepartmentRepository;
import com.hospital.exception.BadRequestException;
import com.hospital.user.Role;
import com.hospital.user.User;
import com.hospital.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Doctor createDoctor(DoctorCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone number already exists");
        }
        if (doctorRepository.findByLicenseNumber(request.getLicenseNumber()).isPresent()) {
            throw new BadRequestException("License number already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode("doctor123"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRole(Role.DOCTOR);
        user.setEnabled(true);
        user = userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(request.getSpecialization());
        doctor.setQualification(request.getQualification());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setLicenseExpiryDate(request.getLicenseExpiryDate());
        doctor.setYearsOfExperience(request.getYearsOfExperience());
        doctor.setBiography(request.getBiography());
        doctor.setConsultationFee(request.getConsultationFee());
        doctor.setFollowUpFee(request.getFollowUpFee());
        doctor.setSlotDurationMinutes(request.getSlotDurationMinutes());
        doctor.setMaxPatientsPerDay(request.getMaxPatientsPerDay());

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new BadRequestException("Department not found"));
            doctor.setDepartment(department);
        }

        doctor = doctorRepository.save(doctor);
        log.info("New doctor created with license: {}", doctor.getLicenseNumber());
        return doctor;
    }

    public Doctor getDoctorById(UUID id) {
        return doctorRepository.findById(id)
                .filter(d -> !d.isDeleted())
                .orElseThrow(() -> new BadRequestException("Doctor not found"));
    }

    public Doctor getDoctorByUserId(UUID userId) {
        return doctorRepository.findByUserId(userId)
                .filter(d -> !d.isDeleted())
                .orElseThrow(() -> new BadRequestException("Doctor profile not found"));
    }

    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAllActive(pageable);
    }

    public Page<Doctor> searchDoctors(String keyword, Pageable pageable) {
        return doctorRepository.searchDoctors(keyword, pageable);
    }

    public Page<Doctor> getDoctorsByDepartment(UUID departmentId, Pageable pageable) {
        return doctorRepository.findAllActive(pageable);
    }

    @Transactional
    public Doctor updateDoctor(UUID id, DoctorUpdateRequest request) {
        Doctor doctor = getDoctorById(id);

        if (request.getSpecialization() != null) doctor.setSpecialization(request.getSpecialization());
        if (request.getQualification() != null) doctor.setQualification(request.getQualification());
        if (request.getBiography() != null) doctor.setBiography(request.getBiography());
        if (request.getConsultationFee() != null) doctor.setConsultationFee(request.getConsultationFee());
        if (request.getFollowUpFee() != null) doctor.setFollowUpFee(request.getFollowUpFee());
        if (request.getMaxPatientsPerDay() != null) doctor.setMaxPatientsPerDay(request.getMaxPatientsPerDay());
        if (request.getSlotDurationMinutes() != null) doctor.setSlotDurationMinutes(request.getSlotDurationMinutes());

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new BadRequestException("Department not found"));
            doctor.setDepartment(department);
        }

        log.info("Doctor updated: {}", doctor.getLicenseNumber());
        return doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(UUID id) {
        Doctor doctor = getDoctorById(id);
        doctor.softDelete();
        doctorRepository.save(doctor);
        log.info("Doctor soft deleted: {}", doctor.getLicenseNumber());
    }
}