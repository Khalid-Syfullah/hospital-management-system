package com.hospital.lab;

import com.hospital.doctor.DoctorService;
import com.hospital.exception.BadRequestException;
import com.hospital.patient.PatientService;
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
public class LabOrderService {

    private final LabOrderRepository labOrderRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Transactional
    public LabOrder createLabOrder(LabOrderCreateRequest request) {
        var patient = patientService.getPatientById(request.getPatientId());
        var doctor = doctorService.getDoctorById(request.getDoctorId());

        LabOrder labOrder = new LabOrder();
        labOrder.setPatient(patient);
        labOrder.setDoctor(doctor);
        labOrder.setTestName(request.getTestName());
        labOrder.setTestCode(request.getTestCode());
        labOrder.setDescription(request.getDescription());
        labOrder.setOrderDate(LocalDateTime.now());
        labOrder.setStatus(LabOrder.LabStatus.REQUESTED);

        labOrder = labOrderRepository.save(labOrder);
        log.info("Lab order created: {} for patient {}", labOrder.getId(), patient.getMrn());
        return labOrder;
    }

    public LabOrder getLabOrderById(UUID id) {
        return labOrderRepository.findById(id).filter(l -> !l.isDeleted())
                .orElseThrow(() -> new BadRequestException("Lab order not found"));
    }

    public List<LabOrder> getLabOrdersByPatient(UUID patientId) {
        return labOrderRepository.findByPatientId(patientId);
    }

    public Page<LabOrder> getAllLabOrders(Pageable pageable) {
        return labOrderRepository.findAllActive(pageable);
    }

    @Transactional
    public LabOrder startLabOrder(UUID id) {
        LabOrder labOrder = getLabOrderById(id);
        labOrder.setStatus(LabOrder.LabStatus.IN_PROGRESS);
        return labOrderRepository.save(labOrder);
    }

    @Transactional
    public LabOrder completeLabOrder(UUID id, String results) {
        LabOrder labOrder = getLabOrderById(id);
        labOrder.setStatus(LabOrder.LabStatus.COMPLETED);
        labOrder.setResults(results);
        labOrder.setCompletedDate(LocalDateTime.now());
        return labOrderRepository.save(labOrder);
    }

    @Transactional
    public LabOrder cancelLabOrder(UUID id) {
        LabOrder labOrder = getLabOrderById(id);
        labOrder.setStatus(LabOrder.LabStatus.CANCELLED);
        return labOrderRepository.save(labOrder);
    }
}