package com.hospital.prescription;

import com.hospital.doctor.Doctor;
import com.hospital.doctor.DoctorRepository;
import com.hospital.exception.DoctorNotFoundException;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PrescriptionMapper mapper;

    @Transactional
    public PrescriptionResponse createPrescription(PrescriptionRequest request) {
        Patient patient = patientRepository.findByIdAndDeletedAtIsNull(request.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException(request.getPatientId()));
        Doctor doctor = doctorRepository.findByIdAndDeletedAtIsNull(request.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException(request.getDoctorId()));

        Prescription prescription = Prescription.builder()
                .patient(patient)
                .doctor(doctor)
                .prescriptionDate(request.getPrescriptionDate())
                .expiryDate(request.getExpiryDate())
                .notes(request.getNotes())
                .build();

        List<PrescriptionItem> items = request.getItems().stream()
                .map(itemReq -> PrescriptionItem.builder()
                        .prescription(prescription)
                        .medicineName(itemReq.getMedicineName())
                        .dosage(itemReq.getDosage())
                        .frequency(itemReq.getFrequency())
                        .duration(itemReq.getDuration())
                        .instructions(itemReq.getInstructions())
                        .quantity(itemReq.getQuantity())
                        .build())
                .toList();
        prescription.setItems(items);

        return mapper.toResponse(prescriptionRepository.save(prescription));
    }

    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescriptionById(UUID id) {
        return mapper.toResponse(findActivePrescription(id));
    }

    @Transactional(readOnly = true)
    public Page<PrescriptionResponse> getByPatient(UUID patientId, Pageable pageable) {
        return prescriptionRepository.findByPatientIdAndDeletedAtIsNull(patientId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PrescriptionResponse> getByDoctor(UUID doctorId, Pageable pageable) {
        return prescriptionRepository.findByDoctorIdAndDeletedAtIsNull(doctorId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public void deletePrescription(UUID id) {
        Prescription prescription = findActivePrescription(id);
        prescription.softDelete();
        prescriptionRepository.save(prescription);
    }

    private Prescription findActivePrescription(UUID id) {
        return prescriptionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", id));
    }
}
