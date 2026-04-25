package com.hospital.patient;

import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.PatientNotFoundException;
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
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        if (request.getEmail() != null && patientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered for a patient: " + request.getEmail());
        }
        Patient patient = patientMapper.toEntity(request);
        patient.setMrn(generateMrn());
        return patientMapper.toResponse(patientRepository.save(patient));
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientById(UUID id) {
        return patientMapper.toResponse(findActivePatient(id));
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientByMrn(String mrn) {
        return patientMapper.toResponse(
                patientRepository.findByMrnAndDeletedAtIsNull(mrn)
                        .orElseThrow(() -> new PatientNotFoundException("Patient not found with MRN: " + mrn))
        );
    }

    @Transactional(readOnly = true)
    public Page<PatientResponse> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(patientMapper::toResponse);
    }

    @Transactional
    public PatientResponse updatePatient(UUID id, PatientRequest request) {
        Patient patient = findActivePatient(id);
        patientMapper.updateEntity(request, patient);
        return patientMapper.toResponse(patientRepository.save(patient));
    }

    @Transactional
    public void deletePatient(UUID id) {
        Patient patient = findActivePatient(id);
        patient.softDelete();
        patientRepository.save(patient);
        log.info("Patient {} soft-deleted", id);
    }

    private Patient findActivePatient(UUID id) {
        return patientRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    private synchronized String generateMrn() {
        int next = patientRepository.findMaxMrnSequence() + 1;
        return String.format("MRN%07d", next);
    }
}
