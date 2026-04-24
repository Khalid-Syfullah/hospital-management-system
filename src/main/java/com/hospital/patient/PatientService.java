package com.hospital.patient;

import com.hospital.audit.AuditService;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.patient.PatientDtos.PatientRequest;
import com.hospital.patient.PatientDtos.PatientResponse;
import java.time.Year;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AuditService auditService;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper, AuditService auditService) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.auditService = auditService;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public PatientResponse create(PatientRequest request) {
        Patient patient = new Patient(generateMrn(), request.fullName(), request.dateOfBirth(), request.phone());
        patient.update(request.fullName(), request.dateOfBirth(), request.phone(), request.emergencyContact(),
                request.insuranceProvider(), request.insurancePolicyNumber(), request.medicalHistory(),
                request.allergies(), request.chronicConditions());
        Patient saved = patientRepository.save(patient);
        auditService.record("Patient", saved.getId() == null ? "pending" : saved.getId().toString(), "CREATE", "Patient registered", null);
        return patientMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST')")
    public Page<PatientResponse> list(Pageable pageable) {
        return patientRepository.findByDeletedAtIsNull(pageable).map(patientMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST','PATIENT')")
    public PatientResponse get(UUID id) {
        return patientMapper.toResponse(findActive(id));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public PatientResponse update(UUID id, PatientRequest request) {
        Patient patient = findActive(id);
        patient.update(request.fullName(), request.dateOfBirth(), request.phone(), request.emergencyContact(),
                request.insuranceProvider(), request.insurancePolicyNumber(), request.medicalHistory(),
                request.allergies(), request.chronicConditions());
        auditService.record("Patient", id.toString(), "UPDATE", "Patient profile updated", null);
        return patientMapper.toResponse(patient);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        Patient patient = findActive(id);
        patient.softDelete();
        auditService.record("Patient", id.toString(), "DELETE", "Patient soft deleted", null);
    }

    public Patient findActive(UUID id) {
        return patientRepository.findById(id)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
    }

    private String generateMrn() {
        return "MRN-" + Year.now().getValue() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
