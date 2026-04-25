package com.hospital.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hospital.audit.AuditService;
import com.hospital.user.UserRepository;
import org.junit.jupiter.api.Test;

class PatientServiceTest {
    @Test
    void createGeneratesMrn() {
        PatientRepository repository = mock(PatientRepository.class);
        when(repository.countByMrnStartingWith(org.mockito.ArgumentMatchers.anyString())).thenReturn(41L);
        when(repository.save(any(Patient.class))).thenAnswer(invocation -> {
            Patient patient = invocation.getArgument(0);
            patient.setId(java.util.UUID.randomUUID());
            return patient;
        });
        PatientService service = new PatientService(repository, mock(UserRepository.class), mock(AuditService.class));

        PatientResponse response = service.create(new PatientRequest("Jane Patient", Patient.Gender.FEMALE,
                null, null, null, null, null, null, null, null, null, null, null, null));

        assertThat(response.mrn()).endsWith("000042");
        assertThat(response.fullName()).isEqualTo("Jane Patient");
    }
}
