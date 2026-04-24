package com.hospital.lab;

import com.hospital.exception.ResourceNotFoundException;
import com.hospital.lab.LabDtos.LabOrderRequest;
import com.hospital.lab.LabDtos.LabOrderResponse;
import com.hospital.lab.LabDtos.LabResultRequest;
import com.hospital.patient.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LabService {
    private final LabOrderRepository repository;
    private final PatientService patientService;
    private final LabOrderMapper mapper;

    public LabService(LabOrderRepository repository, PatientService patientService, LabOrderMapper mapper) {
        this.repository = repository;
        this.patientService = patientService;
        this.mapper = mapper;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE')")
    public LabOrderResponse create(LabOrderRequest request) {
        return mapper.toResponse(repository.save(new LabOrder(patientService.findActive(request.patientId()), request.testName())));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','LAB_TECHNICIAN')")
    public LabOrderResponse complete(java.util.UUID id, LabResultRequest request) {
        LabOrder order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lab order not found"));
        order.complete(request.result());
        return mapper.toResponse(order);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','LAB_TECHNICIAN')")
    public Page<LabOrderResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }
}
