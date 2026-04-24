package com.hospital.prescription;

import com.hospital.doctor.DoctorRepository;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.patient.PatientService;
import com.hospital.prescription.PrescriptionDtos.PrescriptionRequest;
import com.hospital.prescription.PrescriptionDtos.PrescriptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrescriptionService {
    private final PrescriptionRepository repository;
    private final PatientService patientService;
    private final DoctorRepository doctorRepository;
    private final PrescriptionMapper mapper;

    public PrescriptionService(PrescriptionRepository repository, PatientService patientService, DoctorRepository doctorRepository, PrescriptionMapper mapper) {
        this.repository = repository;
        this.patientService = patientService;
        this.doctorRepository = doctorRepository;
        this.mapper = mapper;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public PrescriptionResponse create(PrescriptionRequest request) {
        var doctor = doctorRepository.findById(request.doctorId()).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return mapper.toResponse(repository.save(new Prescription(patientService.findActive(request.patientId()), doctor,
                request.medicineName(), request.dosage(), request.frequency(), request.duration(), request.instructions())));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','PHARMACIST')")
    public Page<PrescriptionResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }
}
