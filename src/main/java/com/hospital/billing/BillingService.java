package com.hospital.billing;

import com.hospital.audit.AuditService;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.patient.PatientService;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingService {
    private final InvoiceRepository repository; private final PatientService patients; private final AuditService audit;
    public BillingService(InvoiceRepository repository, PatientService patients, AuditService audit) { this.repository = repository; this.patients = patients; this.audit = audit; }
    @Transactional(readOnly = true) public Page<InvoiceResponse> list(Pageable pageable) { return repository.findAll(pageable).map(InvoiceResponse::from); }
    @Transactional(readOnly = true) public InvoiceResponse get(UUID id) { return InvoiceResponse.from(find(id)); }
    @Transactional public InvoiceResponse create(InvoiceRequest request) {
        Invoice invoice = new Invoice(); invoice.setPatient(patients.find(request.patientId())); invoice.setInsuranceClaimNumber(request.insuranceClaimNumber());
        BigDecimal total = BigDecimal.ZERO;
        for (InvoiceItemRequest itemRequest : request.items()) {
            InvoiceItem item = new InvoiceItem(); item.setInvoice(invoice); item.setDescription(itemRequest.description()); item.setUnitPrice(itemRequest.unitPrice()); item.setQuantity(itemRequest.quantity());
            item.setLineTotal(itemRequest.unitPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()))); total = total.add(item.getLineTotal()); invoice.getItems().add(item);
        }
        invoice.setTotal(total); repository.save(invoice); audit.record("Invoice", invoice.getId().toString(), "CREATE", "created"); return InvoiceResponse.from(invoice);
    }
    @Transactional public InvoiceResponse pay(UUID id, String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) throw new BadRequestException("Idempotency-Key header is required");
        var existing = repository.findByPaymentIdempotencyKey(idempotencyKey); if (existing.isPresent()) return InvoiceResponse.from(existing.get());
        Invoice invoice = find(id);
        invoice.setStatus(Invoice.PaymentStatus.PAID); invoice.setPaymentIdempotencyKey(idempotencyKey); audit.record("Invoice", id.toString(), "PAYMENT", "paid"); return InvoiceResponse.from(invoice);
    }
    @Transactional public InvoiceResponse cancel(UUID id) { Invoice invoice = find(id); invoice.setStatus(Invoice.PaymentStatus.CANCELLED); audit.record("Invoice", id.toString(), "UPDATE", "cancelled"); return InvoiceResponse.from(invoice); }
    private Invoice find(UUID id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invoice", id)); }
}
