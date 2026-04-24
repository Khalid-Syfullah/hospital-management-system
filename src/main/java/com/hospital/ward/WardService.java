package com.hospital.ward;

import com.hospital.exception.ResourceNotFoundException;
import com.hospital.patient.PatientService;
import com.hospital.ward.WardDtos.AdmissionRequest;
import com.hospital.ward.WardDtos.BedRequest;
import com.hospital.ward.WardDtos.BedResponse;
import com.hospital.ward.WardDtos.WardRequest;
import com.hospital.ward.WardDtos.WardResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WardService {
    private final WardRepository wardRepository;
    private final BedRepository bedRepository;
    private final PatientService patientService;
    private final WardMapper mapper;

    public WardService(WardRepository wardRepository, BedRepository bedRepository, PatientService patientService, WardMapper mapper) {
        this.wardRepository = wardRepository;
        this.bedRepository = bedRepository;
        this.patientService = patientService;
        this.mapper = mapper;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','NURSE')")
    public WardResponse createWard(WardRequest request) {
        return mapper.toResponse(wardRepository.save(new Ward(request.name(), request.type())));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','NURSE')")
    public BedResponse createBed(BedRequest request) {
        Ward ward = wardRepository.findById(request.wardId()).orElseThrow(() -> new ResourceNotFoundException("Ward not found"));
        return mapper.toResponse(bedRepository.save(new Bed(ward, request.bedNumber())));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','NURSE')")
    public BedResponse admit(UUID bedId, AdmissionRequest request) {
        Bed bed = bedRepository.findById(bedId).orElseThrow(() -> new ResourceNotFoundException("Bed not found"));
        bed.admit(patientService.findActive(request.patientId()));
        return mapper.toResponse(bed);
    }

    @Transactional(readOnly = true)
    public Page<BedResponse> listBeds(Pageable pageable) {
        return bedRepository.findAll(pageable).map(mapper::toResponse);
    }
}
