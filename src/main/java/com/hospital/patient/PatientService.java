package com.hospital.patient;

import com.hospital.common.IdGenerator;
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

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;

    @Transactional
    public Patient createPatient(PatientCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone number already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode("patient123"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRole(Role.PATIENT);
        user.setEnabled(true);
        user = userRepository.save(user);

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setMrn(idGenerator.generateMRN());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(Patient.Gender.valueOf(request.getGender().toUpperCase()));
        patient.setBloodType(request.getBloodType());
        patient.setAddress(request.getAddress());
        patient.setCity(request.getCity());
        patient.setState(request.getState());
        patient.setZipCode(request.getZipCode());
        patient.setCountry(request.getCountry());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        patient.setEmergencyContactRelationship(request.getEmergencyContactRelationship());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setAllergies(request.getAllergies());
        patient.setChronicConditions(request.getChronicConditions());
        patient.setInsuranceProvider(request.getInsuranceProvider());
        patient.setInsurancePolicyNumber(request.getInsurancePolicyNumber());
        patient.setInsuranceExpiryDate(request.getInsuranceExpiryDate());

        patient = patientRepository.save(patient);
        log.info("New patient created with MRN: {}", patient.getMrn());
        return patient;
    }

    public Patient getPatientById(UUID id) {
        return patientRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new BadRequestException("Patient not found"));
    }

    public Patient getPatientByMrn(String mrn) {
        return patientRepository.findByMrn(mrn)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new BadRequestException("Patient not found with MRN: " + mrn));
    }

    public Patient getPatientByUserId(UUID userId) {
        return patientRepository.findByUserId(userId)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new BadRequestException("Patient profile not found"));
    }

    public Page<Patient> getAllPatients(Pageable pageable) {
        return patientRepository.findAllActive(pageable);
    }

    public Page<Patient> searchPatients(String keyword, Pageable pageable) {
        return patientRepository.searchPatients(keyword, pageable);
    }

    @Transactional
    public Patient updatePatient(UUID id, PatientUpdateRequest request) {
        Patient patient = getPatientById(id);

        if (request.getAddress() != null) patient.setAddress(request.getAddress());
        if (request.getCity() != null) patient.setCity(request.getCity());
        if (request.getState() != null) patient.setState(request.getState());
        if (request.getZipCode() != null) patient.setZipCode(request.getZipCode());
        if (request.getCountry() != null) patient.setCountry(request.getCountry());
        if (request.getEmergencyContactName() != null) patient.setEmergencyContactName(request.getEmergencyContactName());
        if (request.getEmergencyContactPhone() != null) patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        if (request.getEmergencyContactRelationship() != null) patient.setEmergencyContactRelationship(request.getEmergencyContactRelationship());
        if (request.getMedicalHistory() != null) patient.setMedicalHistory(request.getMedicalHistory());
        if (request.getAllergies() != null) patient.setAllergies(request.getAllergies());
        if (request.getChronicConditions() != null) patient.setChronicConditions(request.getChronicConditions());
        if (request.getInsuranceProvider() != null) patient.setInsuranceProvider(request.getInsuranceProvider());
        if (request.getInsurancePolicyNumber() != null) patient.setInsurancePolicyNumber(request.getInsurancePolicyNumber());
        if (request.getInsuranceExpiryDate() != null) patient.setInsuranceExpiryDate(request.getInsuranceExpiryDate());

        log.info("Patient updated: {}", patient.getMrn());
        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(UUID id) {
        Patient patient = getPatientById(id);
        patient.softDelete();
        patientRepository.save(patient);
        log.info("Patient soft deleted: {}", patient.getMrn());
    }
}