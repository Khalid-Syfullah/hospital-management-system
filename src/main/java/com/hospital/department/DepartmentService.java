package com.hospital.department;

import com.hospital.exception.BadRequestException;
import com.hospital.user.User;
import com.hospital.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Department createDepartment(DepartmentCreateRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new BadRequestException("Department already exists");
        }

        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setFloorNumber(request.getFloorNumber());
        department.setBuilding(request.getBuilding());
        department.setPhoneExtension(request.getPhoneExtension());
        department.setConsultationRooms(request.getConsultationRooms());
        department.setActive(true);

        if (request.getHeadDoctorId() != null) {
            department.setHeadDoctor(userRepository.findById(request.getHeadDoctorId()).orElse(null));
        }

        department = departmentRepository.save(department);
        log.info("New department created: {}", department.getName());
        return department;
    }

    public Department getDepartmentById(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Department not found"));
    }

    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new BadRequestException("Department not found"));
    }

    public Page<Department> getAllDepartments(Pageable pageable) {
        return departmentRepository.findByActive(true, pageable);
    }

    @Transactional
    public Department updateDepartment(UUID id, DepartmentUpdateRequest request) {
        Department department = getDepartmentById(id);

        if (request.getName() != null && !request.getName().equals(department.getName())) {
            if (departmentRepository.existsByName(request.getName())) {
                throw new BadRequestException("Department name already exists");
            }
            department.setName(request.getName());
        }
        if (request.getDescription() != null) department.setDescription(request.getDescription());
        if (request.getFloorNumber() != null) department.setFloorNumber(request.getFloorNumber());
        if (request.getBuilding() != null) department.setBuilding(request.getBuilding());
        if (request.getPhoneExtension() != null) department.setPhoneExtension(request.getPhoneExtension());
        if (request.getConsultationRooms() != null) department.setConsultationRooms(request.getConsultationRooms());

        if (request.getHeadDoctorId() != null) {
            department.setHeadDoctor(userRepository.findById(request.getHeadDoctorId()).orElse(null));
        }

        log.info("Department updated: {}", department.getName());
        return departmentRepository.save(department);
    }

    @Transactional
    public void deleteDepartment(UUID id) {
        Department department = getDepartmentById(id);
        department.setActive(false);
        departmentRepository.save(department);
        log.info("Department deactivated: {}", department.getName());
    }
}