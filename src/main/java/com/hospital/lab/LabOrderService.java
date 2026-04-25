package com.hospital.lab;

import com.hospital.audit.AuditService;
import com.hospital.patient.PatientService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LabOrderService {
    private final LabOrderRepository repository; private final PatientService patients; private final AuditService audit;
    public LabOrderService(LabOrderRepository repository, PatientService patients, AuditService audit) { this.repository = repository; this.patients = patients; this.audit = audit; }
    @Transactional(readOnly = true) public Page<LabOrderResponse> list(Pageable pageable) { return repository.findAll(pageable).map(LabOrderResponse::from); }
    @Transactional(readOnly = true) public LabOrderResponse get(UUID id) { return LabOrderResponse.from(find(id)); }
    @Transactional public LabOrderResponse create(LabOrderRequest r) { LabOrder o = new LabOrder(); o.setPatient(patients.find(r.patientId())); o.setTestName(r.testName()); if (r.status() != null) o.setStatus(r.status()); o.setResult(r.result()); repository.save(o); audit.record("LabOrder", o.getId().toString(), "CREATE", "created"); return LabOrderResponse.from(o); }
    @Transactional public LabOrderResponse update(UUID id, LabOrderRequest r) { LabOrder o = find(id); o.setPatient(patients.find(r.patientId())); o.setTestName(r.testName()); if (r.status() != null) o.setStatus(r.status()); o.setResult(r.result()); audit.record("LabOrder", id.toString(), "UPDATE", "updated"); return LabOrderResponse.from(o); }
    @Transactional public LabOrderResponse cancel(UUID id) { LabOrder o = find(id); o.setStatus(LabOrder.Status.CANCELLED); audit.record("LabOrder", id.toString(), "UPDATE", "cancelled"); return LabOrderResponse.from(o); }
    private LabOrder find(UUID id) { return repository.findById(id).orElseThrow(() -> new com.hospital.exception.ResourceNotFoundException("LabOrder", id)); }
}
