package com.hospital.billing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hospital.audit.AuditService;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BillingServiceTest {
    @Test
    void createCalculatesTotal() {
        InvoiceRepository repository = mock(InvoiceRepository.class);
        when(repository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            invoice.setId(UUID.randomUUID());
            return invoice;
        });
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID());
        PatientService patients = mock(PatientService.class);
        when(patients.find(patient.getId())).thenReturn(patient);
        BillingService service = new BillingService(repository, patients, mock(AuditService.class));

        InvoiceResponse response = service.create(new InvoiceRequest(patient.getId(), null, List.of(
                new InvoiceItemRequest("Consultation", new BigDecimal("100.00"), 1),
                new InvoiceItemRequest("Lab", new BigDecimal("25.50"), 2))));

        assertThat(response.total()).isEqualByComparingTo("151.00");
    }

    @Test
    void payIsIdempotent() {
        InvoiceRepository repository = mock(InvoiceRepository.class);
        Invoice invoice = new Invoice();
        invoice.setId(UUID.randomUUID());
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID());
        invoice.setPatient(patient);
        invoice.setStatus(Invoice.PaymentStatus.PAID);
        invoice.setPaymentIdempotencyKey("pay-1");
        when(repository.findByPaymentIdempotencyKey("pay-1")).thenReturn(Optional.of(invoice));
        BillingService service = new BillingService(repository, mock(PatientService.class), mock(AuditService.class));

        InvoiceResponse response = service.pay(UUID.randomUUID(), "pay-1");

        assertThat(response.status()).isEqualTo(Invoice.PaymentStatus.PAID);
    }
}
