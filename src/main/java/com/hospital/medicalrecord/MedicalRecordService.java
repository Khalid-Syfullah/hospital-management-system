package com.hospital.medicalrecord;

import com.hospital.appointment.Appointment;
import com.hospital.appointment.AppointmentRepository;
import com.hospital.doctor.Doctor;
import com.hospital.doctor.DoctorService;
import com.hospital.exception.BadRequestException;
import com.hospital.patient.Patient;
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
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public MedicalRecord createMedicalRecord(MedicalRecordCreateRequest request) {
        Patient patient = patientService.getPatientById(request.getPatientId());
        Doctor doctor = doctorService.getDoctorById(request.getDoctorId());

        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setVisitDate(request.getVisitDate() != null ? request.getVisitDate() : LocalDateTime.now());
        record.setChiefComplaint(request.getChiefComplaint());
        record.setSymptoms(request.getSymptoms());
        record.setDiagnosis(request.getDiagnosis());
        record.setIcd10Code(request.getIcd10Code());
        record.setTreatmentPlan(request.getTreatmentPlan());
        record.setFollowUpInstructions(request.getFollowUpInstructions());
        record.setBloodPressure(request.getBloodPressure());
        record.setHeartRate(request.getHeartRate());
        record.setTemperature(request.getTemperature());
        record.setWeight(request.getWeight());
        record.setHeight(request.getHeight());
        record.setOxygenSaturation(request.getOxygenSaturation());
        record.setAttachmentPath(request.getAttachmentPath());

        if (request.getAppointmentId() != null) {
            Appointment appt = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new BadRequestException("Appointment not found"));
            record.setAppointment(appt);
        }

        record = medicalRecordRepository.save(record);
        log.info("Medical record created: {} for patient {}", record.getId(), patient.getMrn());
        return record;
    }

    public MedicalRecord getMedicalRecordById(UUID id) {
        return medicalRecordRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new BadRequestException("Medical record not found"));
    }

    public List<MedicalRecord> getMedicalRecordsByPatient(UUID patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    public Page<MedicalRecord> getAllMedicalRecords(Pageable pageable) {
        return medicalRecordRepository.findAllActive(pageable);
    }

    @Transactional
    public MedicalRecord updateMedicalRecord(UUID id, MedicalRecordUpdateRequest request) {
        MedicalRecord record = getMedicalRecordById(id);

        if (request.getDiagnosis() != null) record.setDiagnosis(request.getDiagnosis());
        if (request.getIcd10Code() != null) record.setIcd10Code(request.getIcd10Code());
        if (request.getTreatmentPlan() != null) record.setTreatmentPlan(request.getTreatmentPlan());
        if (request.getFollowUpInstructions() != null) record.setFollowUpInstructions(request.getFollowUpInstructions());

        log.info("Medical record updated: {}", record.getId());
        return medicalRecordRepository.save(record);
    }
}