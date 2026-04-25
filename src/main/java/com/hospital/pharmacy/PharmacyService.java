package com.hospital.pharmacy;

import com.hospital.audit.AuditService;
import com.hospital.exception.InsufficientStockException;
import com.hospital.exception.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PharmacyService {
    private final MedicationRepository repository; private final AuditService audit;
    public PharmacyService(MedicationRepository repository, AuditService audit) { this.repository = repository; this.audit = audit; }
    @Cacheable("medications") @Transactional(readOnly = true) public Page<MedicationResponse> list(Pageable pageable) { return repository.findAll(pageable).map(MedicationResponse::from); }
    @Transactional(readOnly = true) public MedicationResponse get(UUID id) { return MedicationResponse.from(find(id)); }
    @CacheEvict(value = "medications", allEntries = true) @Transactional public MedicationResponse create(MedicationRequest r) { Medication m = new Medication(); apply(m, r); repository.save(m); audit.record("Medication", m.getId().toString(), "CREATE", "created"); return MedicationResponse.from(m); }
    @CacheEvict(value = "medications", allEntries = true) @Transactional public MedicationResponse update(UUID id, MedicationRequest r) { Medication m = find(id); apply(m, r); audit.record("Medication", id.toString(), "UPDATE", "updated"); return MedicationResponse.from(m); }
    @CacheEvict(value = "medications", allEntries = true) @Transactional public MedicationResponse dispense(UUID id, DispenseRequest r) { Medication m = find(id); if (m.getStockQuantity() < r.quantity()) throw new InsufficientStockException("Insufficient medication stock"); m.setStockQuantity(m.getStockQuantity() - r.quantity()); audit.record("Medication", id.toString(), "DISPENSE", "dispensed " + r.quantity()); return MedicationResponse.from(m); }
    @CacheEvict(value = "medications", allEntries = true) @Transactional public void delete(UUID id) { Medication m = find(id); m.softDelete(); audit.record("Medication", id.toString(), "DELETE", "soft deleted"); }
    private void apply(Medication m, MedicationRequest r) { m.setName(r.name()); m.setStrength(r.strength()); m.setForm(r.form()); m.setStockQuantity(r.stockQuantity()); m.setLowStockThreshold(r.lowStockThreshold()); m.setExpiryDate(r.expiryDate()); }
    private Medication find(UUID id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medication", id)); }
}
