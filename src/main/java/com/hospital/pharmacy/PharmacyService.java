package com.hospital.pharmacy;

import com.hospital.exception.InsufficientStockException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientRepository;
import com.hospital.prescription.Prescription;
import com.hospital.prescription.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyService {

    private final MedicationRepository medicationRepository;
    private final DispensingRepository dispensingRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PharmacyMapper mapper;

    @Transactional
    @CacheEvict(value = "medications", allEntries = true)
    public MedicationResponse createMedication(MedicationRequest request) {
        Medication medication = mapper.toEntity(request);
        medication.setActive(true);
        return mapper.toResponse(medicationRepository.save(medication));
    }

    @Transactional(readOnly = true)
    @Cacheable("medications")
    public Page<MedicationResponse> getAllMedications(Pageable pageable) {
        return medicationRepository.findByDeletedAtIsNull(pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public MedicationResponse getMedicationById(UUID id) {
        return mapper.toResponse(findActiveMedication(id));
    }

    @Transactional
    @CacheEvict(value = "medications", allEntries = true)
    public MedicationResponse updateMedication(UUID id, MedicationRequest request) {
        Medication medication = findActiveMedication(id);
        mapper.updateEntity(request, medication);
        return mapper.toResponse(medicationRepository.save(medication));
    }

    @Transactional
    @CacheEvict(value = "medications", allEntries = true)
    public MedicationResponse adjustStock(UUID id, int quantity) {
        Medication medication = findActiveMedication(id);
        int newStock = medication.getStockQuantity() + quantity;
        if (newStock < 0) {
            throw new InsufficientStockException(medication.getName(), Math.abs(quantity), medication.getStockQuantity());
        }
        medication.setStockQuantity(newStock);
        if (medication.getStockQuantity() <= medication.getReorderLevel()) {
            log.warn("Low stock alert for medication: {}, current stock: {}", medication.getName(), medication.getStockQuantity());
        }
        return mapper.toResponse(medicationRepository.save(medication));
    }

    @Transactional
    public void dispenseMedication(DispenseRequest request, String dispensedBy) {
        Patient patient = patientRepository.findByIdAndDeletedAtIsNull(request.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException(request.getPatientId()));

        Medication medication = findActiveMedication(request.getMedicationId());

        if (medication.getStockQuantity() < request.getQuantity()) {
            throw new InsufficientStockException(medication.getName(), request.getQuantity(), medication.getStockQuantity());
        }

        medication.setStockQuantity(medication.getStockQuantity() - request.getQuantity());
        medicationRepository.save(medication);

        Prescription prescription = null;
        if (request.getPrescriptionId() != null) {
            prescription = prescriptionRepository.findByIdAndDeletedAtIsNull(request.getPrescriptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prescription", request.getPrescriptionId()));
        }

        Dispensing dispensing = Dispensing.builder()
                .patient(patient)
                .medication(medication)
                .prescription(prescription)
                .quantityDispensed(request.getQuantity())
                .dispensedAt(LocalDateTime.now())
                .dispensedBy(dispensedBy)
                .notes(request.getNotes())
                .build();

        dispensingRepository.save(dispensing);
    }

    @Transactional(readOnly = true)
    public List<MedicationResponse> getLowStockMedications() {
        return medicationRepository.findLowStockMedications().stream()
                .map(mapper::toResponse)
                .toList();
    }

    private Medication findActiveMedication(UUID id) {
        return medicationRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication", id));
    }
}
