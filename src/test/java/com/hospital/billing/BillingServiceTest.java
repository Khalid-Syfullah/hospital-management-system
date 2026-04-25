package com.hospital.billing;

import com.hospital.patient.Patient;
import com.hospital.patient.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock private BillingRepository billingRepository;
    @Mock private PatientRepository patientRepository;

    private BillingService billingService;

    @BeforeEach
    void setUp() {
        billingService = new BillingService(billingRepository, patientRepository, new BillingMapperImpl());
    }

    private Patient buildPatient() {
        return Patient.builder()
                .mrn("MRN0000001")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    @DisplayName("Create invoice calculates total correctly")
    void createInvoice_calculatesTotal() {
        InvoiceRequest request = new InvoiceRequest();
        request.setPatientId(UUID.randomUUID());
        request.setInvoiceDate(LocalDate.now());
        request.setTaxAmount(new BigDecimal("10.00"));
        request.setDiscountAmount(new BigDecimal("5.00"));

        BillingItemRequest item1 = new BillingItemRequest();
        item1.setDescription("Consultation");
        item1.setItemType(ItemType.CONSULTATION);
        item1.setQuantity(1);
        item1.setUnitPrice(new BigDecimal("100.00"));

        BillingItemRequest item2 = new BillingItemRequest();
        item2.setDescription("Lab Test");
        item2.setItemType(ItemType.LAB_TEST);
        item2.setQuantity(2);
        item2.setUnitPrice(new BigDecimal("50.00"));

        request.setItems(List.of(item1, item2));

        when(patientRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(buildPatient()));
        when(billingRepository.findMaxInvoiceSequence()).thenReturn(0);

        Invoice savedInvoice = Invoice.builder()
                .invoiceNumber("INV00000001")
                .patient(buildPatient())
                .invoiceDate(LocalDate.now())
                .subtotal(new BigDecimal("200.00"))
                .taxAmount(new BigDecimal("10.00"))
                .discountAmount(new BigDecimal("5.00"))
                .totalAmount(new BigDecimal("205.00"))
                .paidAmount(BigDecimal.ZERO)
                .status(PaymentStatus.PENDING)
                .build();

        when(billingRepository.save(any())).thenReturn(savedInvoice);

        InvoiceResponse response = billingService.createInvoice(request);

        assertThat(response.getInvoiceNumber()).isEqualTo("INV00000001");
        assertThat(response.getTotalAmount()).isEqualByComparingTo("205.00");
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    @DisplayName("Idempotency key returns existing invoice")
    void createInvoice_idempotencyKey_returnsExisting() {
        InvoiceRequest request = new InvoiceRequest();
        request.setPatientId(UUID.randomUUID());
        request.setInvoiceDate(LocalDate.now());
        request.setIdempotencyKey("idem-invoice-001");
        request.setItems(List.of());

        Invoice existing = Invoice.builder()
                .invoiceNumber("INV00000001")
                .patient(buildPatient())
                .invoiceDate(LocalDate.now())
                .subtotal(BigDecimal.TEN)
                .taxAmount(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .totalAmount(BigDecimal.TEN)
                .paidAmount(BigDecimal.ZERO)
                .status(PaymentStatus.PENDING)
                .build();

        when(billingRepository.findByIdempotencyKey("idem-invoice-001")).thenReturn(Optional.of(existing));

        InvoiceResponse response = billingService.createInvoice(request);

        assertThat(response.getInvoiceNumber()).isEqualTo("INV00000001");
        verify(billingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Record payment updates status to PAID when fully paid")
    void recordPayment_fullPayment_statusPaid() {
        UUID id = UUID.randomUUID();

        Invoice invoice = Invoice.builder()
                .invoiceNumber("INV00000001")
                .patient(buildPatient())
                .invoiceDate(LocalDate.now())
                .subtotal(new BigDecimal("100.00"))
                .taxAmount(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .totalAmount(new BigDecimal("100.00"))
                .paidAmount(BigDecimal.ZERO)
                .status(PaymentStatus.PENDING)
                .build();

        when(billingRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(invoice));
        when(billingRepository.save(any())).thenReturn(invoice);

        InvoiceResponse response = billingService.recordPayment(id, new BigDecimal("100.00"), null);

        assertThat(response.getStatus()).isEqualTo(PaymentStatus.PAID);
    }

    @Test
    @DisplayName("Record partial payment updates status to PARTIAL")
    void recordPayment_partialPayment_statusPartial() {
        UUID id = UUID.randomUUID();

        Invoice invoice = Invoice.builder()
                .invoiceNumber("INV00000001")
                .patient(buildPatient())
                .invoiceDate(LocalDate.now())
                .subtotal(new BigDecimal("100.00"))
                .taxAmount(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .totalAmount(new BigDecimal("100.00"))
                .paidAmount(BigDecimal.ZERO)
                .status(PaymentStatus.PENDING)
                .build();

        when(billingRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(invoice));
        when(billingRepository.save(any())).thenReturn(invoice);

        InvoiceResponse response = billingService.recordPayment(id, new BigDecimal("50.00"), null);

        assertThat(response.getStatus()).isEqualTo(PaymentStatus.PARTIAL);
    }
}
