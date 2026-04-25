package com.hospital.medicalrecord;

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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalRecordMapper mapper;

    @Transactional
    public MedicalRecordResponse createRecord(MedicalRecordRequest request) {
        Patient patient = patientRepository.findByIdAndDeletedAtIsNull(request.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException(request.getPatientId()));
        Doctor doctor = doctorRepository.findByIdAndDeletedAtIsNull(request.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException(request.getDoctorId()));

        MedicalRecord record = MedicalRecord.builder()
                .patient(patient)
                .doctor(doctor)
                .diagnosis(request.getDiagnosis())
                .icd10Code(request.getIcd10Code())
                .symptoms(request.getSymptoms())
                .visitNotes(request.getVisitNotes())
                .treatmentPlan(request.getTreatmentPlan())
                .attachmentPaths(request.getAttachmentPaths())
                .vitalSigns(request.getVitalSigns())
                .build();

        return mapper.toResponse(medicalRecordRepository.save(record));
    }

    @Transactional(readOnly = true)
    public MedicalRecordResponse getRecordById(UUID id) {
        return mapper.toResponse(findActiveRecord(id));
    }

    @Transactional(readOnly = true)
    public Page<MedicalRecordResponse> getRecordsByPatient(UUID patientId, Pageable pageable) {
        return medicalRecordRepository.findByPatientIdAndDeletedAtIsNull(patientId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<MedicalRecordResponse> getRecordsByDoctor(UUID doctorId, Pageable pageable) {
        return medicalRecordRepository.findByDoctorIdAndDeletedAtIsNull(doctorId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public MedicalRecordResponse updateRecord(UUID id, MedicalRecordRequest request) {
        MedicalRecord record = findActiveRecord(id);
        mapper.updateEntity(request, record);
        return mapper.toResponse(medicalRecordRepository.save(record));
    }

    @Transactional
    public void deleteRecord(UUID id) {
        MedicalRecord record = findActiveRecord(id);
        record.softDelete();
        medicalRecordRepository.save(record);
    }

    private MedicalRecord findActiveRecord(UUID id) {
        return medicalRecordRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord", id));
    }
}
