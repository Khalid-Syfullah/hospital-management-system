package com.hospital.ward;

import com.hospital.department.Department;
import com.hospital.department.DepartmentRepository;
import com.hospital.doctor.Doctor;
import com.hospital.doctor.DoctorRepository;
import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WardService {

    private final WardRepository wardRepository;
    private final BedRepository bedRepository;
    private final AdmissionRepository admissionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final WardMapper mapper;

    @Transactional
    public WardResponse createWard(WardRequest request) {
        if (wardRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Ward already exists: " + request.getName());
        }
        Ward ward = Ward.builder()
                .name(request.getName())
                .wardType(request.getWardType())
                .floor(request.getFloor())
                .description(request.getDescription())
                .build();

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findByIdAndDeletedAtIsNull(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", request.getDepartmentId()));
            ward.setDepartment(dept);
        }

        ward = wardRepository.save(ward);
        return toResponseWithOccupancy(ward);
    }

    @Transactional(readOnly = true)
    public WardResponse getWardById(UUID id) {
        Ward ward = wardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ward", id));
        return toResponseWithOccupancy(ward);
    }

    @Transactional(readOnly = true)
    public Page<WardResponse> getAllWards(Pageable pageable) {
        return wardRepository.findAll(pageable)
                .map(this::toResponseWithOccupancy);
    }

    @Transactional
    public AdmissionResponse admitPatient(AdmissionRequest request) {
        Patient patient = patientRepository.findByIdAndDeletedAtIsNull(request.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException(request.getPatientId()));

        admissionRepository.findByPatientIdAndActiveTrue(request.getPatientId()).ifPresent(a -> {
            throw new IllegalArgumentException("Patient is already admitted");
        });

        Bed bed = bedRepository.findByIdAndDeletedAtIsNull(request.getBedId())
                .orElseThrow(() -> new ResourceNotFoundException("Bed", request.getBedId()));

        if (bed.getStatus() != BedStatus.AVAILABLE) {
            throw new IllegalArgumentException("Bed " + bed.getBedNumber() + " is not available");
        }

        Doctor doctor = null;
        if (request.getDoctorId() != null) {
            doctor = doctorRepository.findByIdAndDeletedAtIsNull(request.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor", request.getDoctorId()));
        }

        bed.setStatus(BedStatus.OCCUPIED);
        bedRepository.save(bed);

        Admission admission = Admission.builder()
                .patient(patient)
                .bed(bed)
                .admittingDoctor(doctor)
                .admissionTime(LocalDateTime.now())
                .admissionReason(request.getAdmissionReason())
                .active(true)
                .build();

        return mapper.toAdmissionResponse(admissionRepository.save(admission));
    }

    @Transactional
    public AdmissionResponse dischargePatient(UUID admissionId, String dischargeNotes) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Admission", admissionId));

        admission.setActive(false);
        admission.setDischargeTime(LocalDateTime.now());
        admission.setDischargeNotes(dischargeNotes);

        admission.getBed().setStatus(BedStatus.AVAILABLE);
        bedRepository.save(admission.getBed());

        return mapper.toAdmissionResponse(admissionRepository.save(admission));
    }

    @Transactional(readOnly = true)
    public Page<AdmissionResponse> getAdmissionsByPatient(UUID patientId, Pageable pageable) {
        return admissionRepository.findByPatientId(patientId, pageable)
                .map(mapper::toAdmissionResponse);
    }

    private WardResponse toResponseWithOccupancy(Ward ward) {
        WardResponse response = mapper.toResponse(ward);
        response.setAvailableBeds(bedRepository.countByWardIdAndStatus(ward.getId(), BedStatus.AVAILABLE));
        response.setOccupiedBeds(bedRepository.countByWardIdAndStatus(ward.getId(), BedStatus.OCCUPIED));
        return response;
    }
}
