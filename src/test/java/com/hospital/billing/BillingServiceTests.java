package com.hospital.billing;

import com.hospital.patient.Patient;
import com.hospital.patient.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BillingServiceTests {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private PatientRepository patientRepository;

    private Patient testPatient;

    @BeforeEach
    public void setup() {
        billingRepository.deleteAll();
        patientRepository.deleteAll();

        testPatient = Patient.builder()
                .firstName("Test")
                .lastName("Patient")
                .mrn("MRN-BILLING-001")
                .phoneNumber("1234567890")
                .active(true)
                .createdBy("system")
                .updatedBy("system")
                .build();
        testPatient = patientRepository.save(testPatient);
    }

    @Test
    public void testCreateInvoice_Success() {
        Billing invoice = Billing.builder()
                .patient(testPatient)
                .invoiceNumber("INV-001")
                .totalAmount(BigDecimal.valueOf(1000.00))
                .paidAmount(BigDecimal.ZERO)
                .status(Billing.PaymentStatus.PENDING)
                .description("Consultation")
                .createdBy("system")
                .updatedBy("system")
                .build();

        invoice = billingRepository.save(invoice);

        assertNotNull(invoice.getId());
        assertEquals("INV-001", invoice.getInvoiceNumber());
        assertEquals(BigDecimal.valueOf(1000.00), invoice.getTotalAmount());
        assertEquals(Billing.PaymentStatus.PENDING, invoice.getStatus());
    }

    @Test
    public void testIdempotencyKey() {
        String key = "idempotent-payment-key";

        Billing invoice = Billing.builder()
                .patient(testPatient)
                .invoiceNumber("INV-002")
                .totalAmount(BigDecimal.valueOf(500.00))
                .paidAmount(BigDecimal.ZERO)
                .status(Billing.PaymentStatus.PENDING)
                .idempotencyKey(key)
                .createdBy("system")
                .updatedBy("system")
                .build();

        invoice = billingRepository.save(invoice);

        Optional<Billing> retrieved = billingRepository.findByIdempotencyKey(key);

        assertTrue(retrieved.isPresent());
        assertEquals(invoice.getId(), retrieved.get().getId());
    }
}
