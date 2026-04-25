package com.hospital.patient;

import com.hospital.exception.BadRequestException;
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
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        Patient patient = patientMapper.toEntity(request);
        patient.setMrn(generateMRN());
        patient.setActive(true);
        patient.setCreatedBy("system");
        patient.setUpdatedBy("system");
        patient = patientRepository.save(patient);
        log.info("Patient created with MRN: {}", patient.getMrn());
        return patientMapper.toResponse(patient);
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatient(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return patientMapper.toResponse(patient);
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientByMrn(String mrn) {
        Patient patient = patientRepository.findByMrn(mrn)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with MRN: " + mrn));
        return patientMapper.toResponse(patient);
    }

    @Transactional
    public PatientResponse updatePatient(UUID id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());
        patient.setCity(request.getCity());
        patient.setState(request.getState());
        patient.setPostalCode(request.getPostalCode());
        patient.setBloodType(request.getBloodType());
        patient.setAllergies(request.getAllergies());
        patient.setChronicConditions(request.getChronicConditions());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        patient.setInsuranceProvider(request.getInsuranceProvider());
        patient.setInsurancePolicyNumber(request.getInsurancePolicyNumber());
        patient.setUpdatedBy("system");
        patient = patientRepository.save(patient);
        log.info("Patient updated: {}", patient.getMrn());
        return patientMapper.toResponse(patient);
    }

    @Transactional
    public void deletePatient(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        patient.setActive(false);
        patientRepository.save(patient);
        log.info("Patient deactivated: {}", patient.getMrn());
    }

    @Transactional(readOnly = true)
    public Page<PatientResponse> searchPatients(String keyword, Pageable pageable) {
        Page<Patient> patients = patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword, pageable);
        List<PatientResponse> responses = patients.getContent().stream()
                .map(patientMapper::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, pageable, patients.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<PatientResponse> getAllPatients(Pageable pageable) {
        Page<Patient> patients = patientRepository.findAll(pageable);
        List<PatientResponse> responses = patients.getContent().stream()
                .map(patientMapper::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, pageable, patients.getTotalElements());
    }

    private String generateMRN() {
        String mrn;
        do {
            mrn = "MRN-" + System.currentTimeMillis();
        } while (patientRepository.existsByMrn(mrn));
        return mrn;
    }
}
