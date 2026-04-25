package com.hospital.pharmacy;

import com.hospital.exception.BadRequestException;
import com.hospital.exception.InsufficientStockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyService {

    private final MedicationRepository medicationRepository;

    @Transactional
    public Medication createMedication(MedicationCreateRequest request) {
        if (medicationRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Medication already exists");
        }

        Medication medication = new Medication();
        medication.setName(request.getName());
        medication.setCode(request.getCode());
        medication.setDescription(request.getDescription());
        medication.setCategory(request.getCategory());
        medication.setManufacturer(request.getManufacturer());
        medication.setUnitPrice(request.getUnitPrice());
        medication.setCurrentStock(request.getCurrentStock());
        medication.setReorderLevel(request.getReorderLevel());
        medication.setExpiryDate(request.getExpiryDate());
        medication.setRequiresPrescription(request.isRequiresPrescription());
        medication.setStorageLocation(request.getStorageLocation());
        medication.setActive(true);

        medication = medicationRepository.save(medication);
        log.info("Medication created: {}", medication.getName());
        return medication;
    }

    public Medication getMedicationById(UUID id) {
        return medicationRepository.findById(id).filter(m -> !m.isDeleted())
                .orElseThrow(() -> new BadRequestException("Medication not found"));
    }

    public List<Medication> getLowStockMedications() {
        return medicationRepository.findLowStock();
    }

    public List<Medication> getExpiringSoon(LocalDate expiryDate) {
        return medicationRepository.findExpiringSoon(expiryDate);
    }

    public Page<Medication> getAllMedications(Pageable pageable) {
        return medicationRepository.findAllActive(pageable);
    }

    public Page<Medication> searchMedications(String keyword, Pageable pageable) {
        return medicationRepository.search(keyword, pageable);
    }

    @Transactional
    public Medication updateStock(UUID id, Integer quantity) {
        Medication medication = getMedicationById(id);
        medication.setCurrentStock(quantity);
        return medicationRepository.save(medication);
    }

    @Transactional
    public void dispense(UUID medicationId, Integer quantity) {
        Medication medication = getMedicationById(medicationId);
        if (medication.getCurrentStock() < quantity) {
            throw new InsufficientStockException("Insufficient stock for " + medication.getName());
        }
        medication.setCurrentStock(medication.getCurrentStock() - quantity);
        medicationRepository.save(medication);
        log.info("Dispensed {} units of {}", quantity, medication.getName());
    }

    @Transactional
    public void restock(UUID id, Integer quantity) {
        Medication medication = getMedicationById(id);
        medication.setCurrentStock(medication.getCurrentStock() + quantity);
        medicationRepository.save(medication);
        log.info("Restocked {} units of {}", quantity, medication.getName());
    }
}