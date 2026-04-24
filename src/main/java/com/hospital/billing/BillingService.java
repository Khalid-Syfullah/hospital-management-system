package com.hospital.billing;

import com.hospital.billing.BillingDtos.CreateInvoiceRequest;
import com.hospital.billing.BillingDtos.InvoiceResponse;
import com.hospital.billing.BillingDtos.PaymentRequest;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingService {
    private final InvoiceRepository invoiceRepository;
    private final PatientService patientService;
    private final BillingMapper billingMapper;

    public BillingService(InvoiceRepository invoiceRepository, PatientService patientService, BillingMapper billingMapper) {
        this.invoiceRepository = invoiceRepository;
        this.patientService = patientService;
        this.billingMapper = billingMapper;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public InvoiceResponse create(CreateInvoiceRequest request) {
        Patient patient = patientService.findActive(request.patientId());
        Invoice invoice = new Invoice(patient, request.insuranceClaimNumber());
        invoice.replaceItems(request.items().stream()
                .map(item -> new BillingItem(item.description(), item.quantity(), item.unitPrice()))
                .toList());
        return billingMapper.toResponse(invoiceRepository.save(invoice));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public Page<InvoiceResponse> list(Pageable pageable) {
        return invoiceRepository.findAll(pageable).map(billingMapper::toResponse);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public InvoiceResponse recordPayment(UUID id, PaymentRequest request, String idempotencyKey) {
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            var existing = invoiceRepository.findByPaymentIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return billingMapper.toResponse(existing.get());
            }
        }
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new com.hospital.exception.ResourceNotFoundException("Invoice not found"));
        invoice.markPayment(request.status(), idempotencyKey);
        return billingMapper.toResponse(invoice);
    }
}
