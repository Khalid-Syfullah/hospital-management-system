package com.hospital.ward;

import com.hospital.audit.AuditService;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.patient.PatientService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WardService {
    private final WardRepository wards; private final BedRepository beds; private final PatientService patients; private final AuditService audit;
    public WardService(WardRepository wards, BedRepository beds, PatientService patients, AuditService audit) { this.wards = wards; this.beds = beds; this.patients = patients; this.audit = audit; }
    @Transactional(readOnly = true) public Page<WardResponse> listWards(Pageable pageable) { return wards.findAll(pageable).map(WardResponse::from); }
    @Transactional(readOnly = true) public Page<BedResponse> listBeds(Pageable pageable) { return beds.findAll(pageable).map(BedResponse::from); }
    @Transactional(readOnly = true) public WardResponse getWard(UUID id) { return WardResponse.from(findWard(id)); }
    @Transactional(readOnly = true) public BedResponse getBed(UUID id) { return BedResponse.from(findBed(id)); }
    @Transactional public WardResponse createWard(WardRequest r) { Ward w = new Ward(); w.setName(r.name()); w.setFloor(r.floor()); wards.save(w); audit.record("Ward", w.getId().toString(), "CREATE", "created"); return WardResponse.from(w); }
    @Transactional public WardResponse updateWard(UUID id, WardRequest r) { Ward w = findWard(id); w.setName(r.name()); w.setFloor(r.floor()); audit.record("Ward", id.toString(), "UPDATE", "updated"); return WardResponse.from(w); }
    @Transactional public BedResponse createBed(BedRequest r) { Bed b = new Bed(); apply(b, r); beds.save(b); audit.record("Bed", b.getId().toString(), "CREATE", "created"); return BedResponse.from(b); }
    @Transactional public BedResponse updateBed(UUID id, BedRequest r) { Bed b = findBed(id); apply(b, r); audit.record("Bed", id.toString(), "UPDATE", "updated"); return BedResponse.from(b); }
    @Transactional public void deleteBed(UUID id) { Bed b = findBed(id); b.softDelete(); audit.record("Bed", id.toString(), "DELETE", "soft deleted"); }
    private void apply(Bed b, BedRequest r) { b.setWard(wards.findById(r.wardId()).orElseThrow(() -> new ResourceNotFoundException("Ward", r.wardId()))); b.setBedNumber(r.bedNumber()); b.setStatus(r.status() == null ? Bed.Status.AVAILABLE : r.status()); b.setPatient(r.patientId() == null ? null : patients.find(r.patientId())); }
    private Ward findWard(UUID id) { return wards.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ward", id)); }
    private Bed findBed(UUID id) { return beds.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bed", id)); }
}
