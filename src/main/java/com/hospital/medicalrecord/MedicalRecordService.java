package com.hospital.medicalrecord;

import com.hospital.medicalrecord.MedicalRecordDtos.MedicalRecordRequest;
import com.hospital.medicalrecord.MedicalRecordDtos.MedicalRecordResponse;
import com.hospital.patient.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepository repository;
    private final PatientService patientService;
    private final MedicalRecordMapper mapper;

    public MedicalRecordService(MedicalRecordRepository repository, PatientService patientService, MedicalRecordMapper mapper) {
        this.repository = repository;
        this.patientService = patientService;
        this.mapper = mapper;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE')")
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        return mapper.toResponse(repository.save(new MedicalRecord(patientService.findActive(request.patientId()),
                request.icd10Code(), request.symptoms(), request.visitNotes(), request.vitals())));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE')")
    public Page<MedicalRecordResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }
}
