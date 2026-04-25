package com.hospital.lab;

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

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LabService {

    private final LabRepository labRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final LabMapper mapper;

    @Transactional
    public LabOrderResponse createLabOrder(LabOrderRequest request) {
        Patient patient = patientRepository.findByIdAndDeletedAtIsNull(request.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException(request.getPatientId()));
        Doctor doctor = doctorRepository.findByIdAndDeletedAtIsNull(request.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException(request.getDoctorId()));

        LabOrder order = LabOrder.builder()
                .patient(patient)
                .doctor(doctor)
                .testName(request.getTestName())
                .testCode(request.getTestCode())
                .clinicalNotes(request.getClinicalNotes())
                .priority(request.getPriority())
                .build();

        return mapper.toResponse(labRepository.save(order));
    }

    @Transactional(readOnly = true)
    public LabOrderResponse getLabOrderById(UUID id) {
        return mapper.toResponse(findActiveLabOrder(id));
    }

    @Transactional(readOnly = true)
    public Page<LabOrderResponse> getByPatient(UUID patientId, Pageable pageable) {
        return labRepository.findByPatientIdAndDeletedAtIsNull(patientId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<LabOrderResponse> getByStatus(LabStatus status, Pageable pageable) {
        return labRepository.findByStatusAndDeletedAtIsNull(status, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public LabOrderResponse updateStatus(UUID id, LabStatus status) {
        LabOrder order = findActiveLabOrder(id);
        order.setStatus(status);
        return mapper.toResponse(labRepository.save(order));
    }

    @Transactional
    public LabOrderResponse addResult(UUID labOrderId, LabResultRequest request) {
        LabOrder order = findActiveLabOrder(labOrderId);

        LabResult result = LabResult.builder()
                .labOrder(order)
                .findings(request.getFindings())
                .referenceRange(request.getReferenceRange())
                .interpretation(request.getInterpretation())
                .resultDateTime(request.getResultDateTime() != null ? request.getResultDateTime() : LocalDateTime.now())
                .performedBy(request.getPerformedBy())
                .attachmentPath(request.getAttachmentPath())
                .build();

        order.setResult(result);
        order.setStatus(LabStatus.COMPLETED);
        return mapper.toResponse(labRepository.save(order));
    }

    private LabOrder findActiveLabOrder(UUID id) {
        return labRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("LabOrder", id));
    }
}
