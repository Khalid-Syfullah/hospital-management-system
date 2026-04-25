package com.hospital.prescription;

import com.hospital.doctor.Doctor;
import com.hospital.doctor.DoctorService;
import com.hospital.exception.BadRequestException;
import com.hospital.medicalrecord.MedicalRecord;
import com.hospital.medicalrecord.MedicalRecordRepository;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final MedicalRecordRepository medicalRecordRepository;

    @Transactional
    public Prescription createPrescription(PrescriptionCreateRequest request) {
        Patient patient = patientService.getPatientById(request.getPatientId());
        Doctor doctor = doctorService.getDoctorById(request.getDoctorId());

        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setPrescriptionDate(LocalDateTime.now());
        prescription.setMedicineName(request.getMedicineName());
        prescription.setDosage(request.getDosage());
        prescription.setFrequency(request.getFrequency());
        prescription.setDurationDays(request.getDurationDays());
        prescription.setInstructions(request.getInstructions());
        prescription.setStartDate(request.getStartDate());
        prescription.setEndDate(request.getEndDate());
        prescription.setStatus(Prescription.PrescriptionStatus.ACTIVE);

        if (request.getMedicalRecordId() != null) {
            MedicalRecord record = medicalRecordRepository.findById(request.getMedicalRecordId())
                    .orElseThrow(() -> new BadRequestException("Medical record not found"));
            prescription.setMedicalRecord(record);
        }

        prescription = prescriptionRepository.save(prescription);
        log.info("Prescription created: {} for patient {}", prescription.getId(), patient.getMrn());
        return prescription;
    }

    public Prescription getPrescriptionById(UUID id) {
        return prescriptionRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new BadRequestException("Prescription not found"));
    }

    public List<Prescription> getPrescriptionsByPatient(UUID patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    public Page<Prescription> getAllPrescriptions(Pageable pageable) {
        return prescriptionRepository.findAllActive(pageable);
    }

    @Transactional
    public Prescription updatePrescription(UUID id, PrescriptionUpdateRequest request) {
        Prescription prescription = getPrescriptionById(id);
        if (request.getDosage() != null) prescription.setDosage(request.getDosage());
        if (request.getFrequency() != null) prescription.setFrequency(request.getFrequency());
        if (request.getInstructions() != null) prescription.setInstructions(request.getInstructions());
        if (request.getStatus() != null) prescription.setStatus(Prescription.PrescriptionStatus.valueOf(request.getStatus()));
        log.info("Prescription updated: {}", prescription.getId());
        return prescriptionRepository.save(prescription);
    }

    @Transactional
    public void cancelPrescription(UUID id) {
        Prescription prescription = getPrescriptionById(id);
        prescription.setStatus(Prescription.PrescriptionStatus.CANCELLED);
        prescriptionRepository.save(prescription);
        log.info("Prescription cancelled: {}", prescription.getId());
    }
}