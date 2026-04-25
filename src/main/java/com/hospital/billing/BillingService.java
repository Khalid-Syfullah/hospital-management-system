package com.hospital.billing;

import com.hospital.exception.PatientNotFoundException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    private final BillingMapper mapper;

    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        if (request.getIdempotencyKey() != null) {
            var existing = billingRepository.findByIdempotencyKey(request.getIdempotencyKey());
            if (existing.isPresent()) {
                return mapper.toResponse(existing.get());
            }
        }

        Patient patient = patientRepository.findByIdAndDeletedAtIsNull(request.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException(request.getPatientId()));

        Invoice invoice = Invoice.builder()
                .invoiceNumber(generateInvoiceNumber())
                .patient(patient)
                .invoiceDate(request.getInvoiceDate())
                .dueDate(request.getDueDate())
                .taxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : BigDecimal.ZERO)
                .discountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO)
                .notes(request.getNotes())
                .idempotencyKey(request.getIdempotencyKey())
                .build();

        List<BillingItem> items = request.getItems().stream()
                .map(itemReq -> BillingItem.builder()
                        .invoice(invoice)
                        .description(itemReq.getDescription())
                        .itemType(itemReq.getItemType())
                        .quantity(itemReq.getQuantity())
                        .unitPrice(itemReq.getUnitPrice())
                        .lineTotal(itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())))
                        .build())
                .toList();
        invoice.setItems(items);
        invoice.recalculateTotal();

        return mapper.toResponse(billingRepository.save(invoice));
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(UUID id) {
        return mapper.toResponse(findActiveInvoice(id));
    }

    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getByPatient(UUID patientId, Pageable pageable) {
        return billingRepository.findByPatientIdAndDeletedAtIsNull(patientId, pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getByStatus(PaymentStatus status, Pageable pageable) {
        return billingRepository.findByStatusAndDeletedAtIsNull(status, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public InvoiceResponse recordPayment(UUID id, BigDecimal amount, String idempotencyKey) {
        if (idempotencyKey != null) {
            var existing = billingRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return mapper.toResponse(existing.get());
            }
        }

        Invoice invoice = findActiveInvoice(id);
        invoice.setPaidAmount(invoice.getPaidAmount().add(amount));

        if (invoice.getPaidAmount().compareTo(invoice.getTotalAmount()) >= 0) {
            invoice.setStatus(PaymentStatus.PAID);
        } else if (invoice.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus(PaymentStatus.PARTIAL);
        }

        return mapper.toResponse(billingRepository.save(invoice));
    }

    @Transactional(readOnly = true)
    public BigDecimal getRevenue(LocalDate from, LocalDate to) {
        return billingRepository.sumRevenueByDateRange(from, to);
    }

    private Invoice findActiveInvoice(UUID id) {
        return billingRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", id));
    }

    private synchronized String generateInvoiceNumber() {
        int next = billingRepository.findMaxInvoiceSequence() + 1;
        return String.format("INV%08d", next);
    }
}
