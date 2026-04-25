package com.hospital.patient;

import com.hospital.audit.AuditService;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.user.UserRepository;
import java.time.Year;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {
    private final PatientRepository repository;
    private final UserRepository users;
    private final AuditService audit;
    public PatientService(PatientRepository repository, UserRepository users, AuditService audit) {
        this.repository = repository; this.users = users; this.audit = audit;
    }
    @Transactional(readOnly = true) public Page<PatientResponse> list(Pageable pageable) { return repository.findAll(pageable).map(PatientResponse::from); }
    @Transactional(readOnly = true) public PatientResponse get(UUID id) { return PatientResponse.from(find(id)); }
    @Transactional public PatientResponse create(PatientRequest request) {
        Patient patient = new Patient();
        patient.setMrn(generateMrn());
        apply(patient, request);
        repository.save(patient);
        audit.record("Patient", patient.getId().toString(), "CREATE", "created");
        return PatientResponse.from(patient);
    }
    @Transactional public PatientResponse update(UUID id, PatientRequest request) {
        Patient patient = find(id);
        apply(patient, request);
        audit.record("Patient", id.toString(), "UPDATE", "updated");
        return PatientResponse.from(patient);
    }
    @Transactional public void delete(UUID id) {
        Patient patient = find(id);
        patient.softDelete();
        audit.record("Patient", id.toString(), "DELETE", "soft deleted");
    }
    private void apply(Patient p, PatientRequest r) {
        p.setFullName(r.fullName()); p.setGender(r.gender() == null ? Patient.Gender.UNKNOWN : r.gender());
        p.setDateOfBirth(r.dateOfBirth()); p.setPhone(r.phone()); p.setEmail(r.email()); p.setAddress(r.address());
        p.setMedicalHistory(r.medicalHistory()); p.setAllergies(r.allergies()); p.setChronicConditions(r.chronicConditions());
        p.setEmergencyContactName(r.emergencyContactName()); p.setEmergencyContactPhone(r.emergencyContactPhone());
        p.setInsuranceProvider(r.insuranceProvider()); p.setInsurancePolicyNumber(r.insurancePolicyNumber());
        p.setUser(r.userId() == null ? null : users.findById(r.userId()).orElseThrow(() -> new ResourceNotFoundException("User", r.userId())));
    }
    private String generateMrn() {
        String prefix = "MRN-" + Year.now().getValue() + "-";
        return prefix + String.format("%06d", repository.countByMrnStartingWith(prefix) + 1);
    }
    public Patient find(UUID id) { return repository.findById(id).orElseThrow(() -> new PatientNotFoundException(id)); }
}
