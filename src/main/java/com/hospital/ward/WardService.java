package com.hospital.ward;

import com.hospital.department.Department;
import com.hospital.department.DepartmentRepository;
import com.hospital.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class WardService {

    private final WardRepository wardRepository;
    private final BedRepository bedRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public Ward createWard(WardCreateRequest request) {
        Ward ward = new Ward();
        ward.setName(request.getName());
        ward.setWardType(request.getWardType());
        ward.setTotalBeds(request.getTotalBeds());
        ward.setAvailableBeds(request.getTotalBeds());
        ward.setActive(true);

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new BadRequestException("Department not found"));
            ward.setDepartment(dept);
        }

        ward = wardRepository.save(ward);

        for (int i = 1; i <= request.getTotalBeds(); i++) {
            Bed bed = new Bed();
            bed.setWard(ward);
            bed.setBedNumber(ward.getName() + "-" + i);
            bed.setStatus(Bed.BedStatus.AVAILABLE);
            bedRepository.save(bed);
        }

        log.info("Ward created: {} with {} beds", ward.getName(), ward.getTotalBeds());
        return ward;
    }

    public Ward getWardById(UUID id) {
        return wardRepository.findById(id).filter(w -> !w.isDeleted())
                .orElseThrow(() -> new BadRequestException("Ward not found"));
    }

    public Page<Ward> getAllWards(Pageable pageable) {
        return wardRepository.findAllActive(pageable);
    }

    public List<Bed> getBedsByWard(UUID wardId) {
        return bedRepository.findByWardId(wardId);
    }

    @Transactional
    public Bed admitPatient(UUID bedId, UUID patientId) {
        Bed bed = bedRepository.findById(bedId).filter(b -> !b.isDeleted())
                .orElseThrow(() -> new BadRequestException("Bed not found"));

        if (bed.getStatus() != Bed.BedStatus.AVAILABLE) {
            throw new BadRequestException("Bed is not available");
        }

        bed.setStatus(Bed.BedStatus.OCCUPIED);
        bed.setAdmittedAt(LocalDateTime.now());

        Ward ward = bed.getWard();
        ward.setAvailableBeds(ward.getAvailableBeds() - 1);
        wardRepository.save(ward);

        return bedRepository.save(bed);
    }

    @Transactional
    public Bed dischargePatient(UUID bedId) {
        Bed bed = bedRepository.findById(bedId).filter(b -> !b.isDeleted())
                .orElseThrow(() -> new BadRequestException("Bed not found"));

        bed.setStatus(Bed.BedStatus.AVAILABLE);
        bed.setDischargedAt(LocalDateTime.now());
        bed.setPatient(null);

        Ward ward = bed.getWard();
        ward.setAvailableBeds(ward.getAvailableBeds() + 1);
        wardRepository.save(ward);

        return bedRepository.save(bed);
    }
}