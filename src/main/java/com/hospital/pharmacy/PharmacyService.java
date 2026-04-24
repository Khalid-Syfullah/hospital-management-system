package com.hospital.pharmacy;

import com.hospital.exception.ResourceNotFoundException;
import com.hospital.pharmacy.PharmacyDtos.DispenseRequest;
import com.hospital.pharmacy.PharmacyDtos.MedicationRequest;
import com.hospital.pharmacy.PharmacyDtos.MedicationResponse;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PharmacyService {
    private final MedicationRepository repository;
    private final MedicationMapper mapper;

    public PharmacyService(MedicationRepository repository, MedicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @CacheEvict(value = "medicationCatalog", allEntries = true)
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public MedicationResponse create(MedicationRequest request) {
        return mapper.toResponse(repository.save(new Medication(request.name(), request.form(), request.stockQuantity(),
                request.lowStockThreshold(), request.expiryDate())));
    }

    @Transactional(readOnly = true)
    @Cacheable("medicationCatalog")
    public Page<MedicationResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "medicationCatalog", allEntries = true)
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    public MedicationResponse dispense(UUID id, DispenseRequest request) {
        Medication medication = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medication not found"));
        medication.dispense(request.quantity());
        return mapper.toResponse(medication);
    }
}
