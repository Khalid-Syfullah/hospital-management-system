package com.hospital.billing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hospital.billing.BillingDtos.BillingItemRequest;
import com.hospital.billing.BillingDtos.CreateInvoiceRequest;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientService;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {
    @Mock
    InvoiceRepository repository;
    @Mock
    PatientService patientService;

    @Test
    void createCalculatesInvoiceTotalFromLineItems() {
        UUID patientId = UUID.randomUUID();
        when(patientService.findActive(patientId)).thenReturn(new Patient("MRN-1", "Patient", null, null));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        BillingService service = new BillingService(repository, patientService, Mappers.getMapper(BillingMapper.class));

        var response = service.create(new CreateInvoiceRequest(patientId, "CLAIM-1", List.of(
                new BillingItemRequest("Consultation", 1, new BigDecimal("100.00")),
                new BillingItemRequest("Lab", 2, new BigDecimal("25.50")))));

        assertThat(response.total()).isEqualByComparingTo("151.00");
        assertThat(response.status()).isEqualTo(PaymentStatus.PENDING);
    }
}
