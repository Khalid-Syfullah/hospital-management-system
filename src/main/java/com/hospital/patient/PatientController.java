package com.hospital.patient;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import com.hospital.patient.PatientDtos.PatientRequest;
import com.hospital.patient.PatientDtos.PatientResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<PatientResponse> create(@Valid @RequestBody PatientRequest request) {
        return ApiResponse.success("Patient created", patientService.create(request));
    }

    @GetMapping
    PageResponse<PatientResponse> list(Pageable pageable) {
        return PageResponse.from("Patients fetched", patientService.list(pageable));
    }

    @GetMapping("/{id}")
    ApiResponse<PatientResponse> get(@PathVariable UUID id) {
        return ApiResponse.success("Patient fetched", patientService.get(id));
    }

    @PutMapping("/{id}")
    ApiResponse<PatientResponse> update(@PathVariable UUID id, @Valid @RequestBody PatientRequest request) {
        return ApiResponse.success("Patient updated", patientService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID id) {
        patientService.delete(id);
    }
}
