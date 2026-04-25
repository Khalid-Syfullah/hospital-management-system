package com.hospital.medicalrecord;

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
public class MedicalRecordService {
    private final MedicalRecordRepository repository; private final PatientService patients; private final DoctorService doctors; private final AuditService audit;
    public MedicalRecordService(MedicalRecordRepository repository, PatientService patients, DoctorService doctors, AuditService audit) { this.repository = repository; this.patients = patients; this.doctors = doctors; this.audit = audit; }
    @Transactional(readOnly = true) public Page<MedicalRecordResponse> list(Pageable pageable) { return repository.findAll(pageable).map(MedicalRecordResponse::from); }
    @Transactional(readOnly = true) public MedicalRecordResponse get(UUID id) { return MedicalRecordResponse.from(find(id)); }
    @Transactional public MedicalRecordResponse create(MedicalRecordRequest request) {
        MedicalRecord r = new MedicalRecord(); apply(r, request); repository.save(r); audit.record("MedicalRecord", r.getId().toString(), "CREATE", "created"); return MedicalRecordResponse.from(r);
    }
    @Transactional public MedicalRecordResponse update(UUID id, MedicalRecordRequest request) {
        MedicalRecord r = find(id); apply(r, request); audit.record("MedicalRecord", id.toString(), "UPDATE", "updated"); return MedicalRecordResponse.from(r);
    }
    @Transactional public void delete(UUID id) {
        MedicalRecord r = find(id); r.softDelete(); audit.record("MedicalRecord", id.toString(), "DELETE", "soft deleted");
    }
    private void apply(MedicalRecord r, MedicalRecordRequest q) {
        r.setPatient(patients.find(q.patientId())); r.setDoctor(doctors.find(q.doctorId())); r.setIcd10Code(q.icd10Code());
        r.setDiagnoses(q.diagnoses()); r.setSymptoms(q.symptoms()); r.setVisitNotes(q.visitNotes()); r.setAttachmentPath(q.attachmentPath());
    }
    private MedicalRecord find(UUID id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MedicalRecord", id)); }
}
