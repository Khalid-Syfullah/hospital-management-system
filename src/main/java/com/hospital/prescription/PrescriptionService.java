package com.hospital.prescription;

import com.hospital.audit.AuditService;
import com.hospital.doctor.DoctorService;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.patient.PatientService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrescriptionService {
    private final PrescriptionRepository repository; private final PatientService patients; private final DoctorService doctors; private final AuditService audit;
    public PrescriptionService(PrescriptionRepository repository, PatientService patients, DoctorService doctors, AuditService audit) { this.repository = repository; this.patients = patients; this.doctors = doctors; this.audit = audit; }
    @Transactional(readOnly = true) public Page<PrescriptionResponse> list(Pageable pageable) { return repository.findAll(pageable).map(PrescriptionResponse::from); }
    @Transactional(readOnly = true) public PrescriptionResponse get(UUID id) { return PrescriptionResponse.from(find(id)); }
    @Transactional public PrescriptionResponse create(PrescriptionRequest r) { Prescription p = new Prescription(); apply(p, r); repository.save(p); audit.record("Prescription", p.getId().toString(), "CREATE", "created"); return PrescriptionResponse.from(p); }
    @Transactional public PrescriptionResponse update(UUID id, PrescriptionRequest r) { Prescription p = find(id); apply(p, r); audit.record("Prescription", id.toString(), "UPDATE", "updated"); return PrescriptionResponse.from(p); }
    @Transactional public PrescriptionResponse cancel(UUID id) { Prescription p = find(id); p.setStatus(Prescription.Status.CANCELLED); audit.record("Prescription", id.toString(), "UPDATE", "cancelled"); return PrescriptionResponse.from(p); }
    private void apply(Prescription p, PrescriptionRequest r) { p.setPatient(patients.find(r.patientId())); p.setDoctor(doctors.find(r.doctorId())); p.setMedicineName(r.medicineName()); p.setDosage(r.dosage()); p.setFrequency(r.frequency()); p.setDuration(r.duration()); p.setInstructions(r.instructions()); }
    private Prescription find(UUID id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Prescription", id)); }
}
