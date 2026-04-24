package com.hospital.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hospital.audit.AuditService;
import com.hospital.patient.PatientDtos.PatientRequest;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock
    PatientRepository repository;
    @Mock
    AuditService auditService;

    @Test
    void createGeneratesMrnAndMapsResponse() {
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        PatientService service = new PatientService(repository, Mappers.getMapper(PatientMapper.class), auditService);

        var response = service.create(new PatientRequest(
                "Jane Patient",
                LocalDate.of(1990, 1, 1),
                "555-1000",
                "Emergency",
                "Insurer",
                "POL-1",
                "History",
                "None",
                "None"));

        assertThat(response.mrn()).startsWith("MRN-");
        assertThat(response.fullName()).isEqualTo("Jane Patient");
    }
}
