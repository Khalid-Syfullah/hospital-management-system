package com.hospital.billing;

import com.hospital.common.IdGenerator;
import com.hospital.exception.BadRequestException;
import com.hospital.patient.Patient;
import com.hospital.patient.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final PatientService patientService;
    private final IdGenerator idGenerator;

    @Transactional
    public Invoice createInvoice(InvoiceCreateRequest request) {
        if (request.getIdempotencyKey() != null) {
            invoiceRepository.findByIdempotencyKey(request.getIdempotencyKey())
                    .ifPresent(existing -> log.info("Returning existing invoice for idempotency key: {}", request.getIdempotencyKey()));
        }

        Patient patient = patientService.getPatientById(request.getPatientId());

        Invoice invoice = new Invoice();
        invoice.setPatient(patient);
        invoice.setInvoiceNumber(idGenerator.generateMRN().replace("MRN", "INV"));
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setDueDate(request.getDueDate());
        invoice.setNotes(request.getNotes());
        invoice.setIdempotencyKey(request.getIdempotencyKey());
        invoice.setStatus(Invoice.PaymentStatus.PENDING);
        invoice.setTax(request.getTax() != null ? request.getTax() : BigDecimal.ZERO);
        invoice.setDiscount(request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO);

        if (request.getItems() != null) {
            for (InvoiceItemRequest itemReq : request.getItems()) {
                InvoiceItem item = new InvoiceItem();
                item.setInvoice(invoice);
                item.setDescription(itemReq.getDescription());
                item.setItemType(itemReq.getItemType());
                item.setQuantity(itemReq.getQuantity());
                item.setUnitPrice(itemReq.getUnitPrice());
                item.setAmount(itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
                invoice.getItems().add(item);
            }
        }

        invoice.calculateTotal();
        invoice = invoiceRepository.save(invoice);
        log.info("Invoice created: {} for patient {}", invoice.getInvoiceNumber(), patient.getMrn());
        return invoice;
    }

    public Invoice getInvoiceById(UUID id) {
        return invoiceRepository.findById(id)
                .filter(i -> !i.isDeleted())
                .orElseThrow(() -> new BadRequestException("Invoice not found"));
    }

    public Invoice getInvoiceByNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new BadRequestException("Invoice not found"));
    }

    public List<Invoice> getInvoicesByPatient(UUID patientId) {
        return invoiceRepository.findByPatientId(patientId);
    }

    public Page<Invoice> getAllInvoices(Pageable pageable) {
        return invoiceRepository.findAllActive(pageable);
    }

    @Transactional
    public Invoice makePayment(UUID id, BigDecimal amount) {
        Invoice invoice = getInvoiceById(id);
        invoice.setPaidAmount(invoice.getPaidAmount().add(amount));

        if (invoice.getPaidAmount().compareTo(invoice.getTotal()) >= 0) {
            invoice.setStatus(Invoice.PaymentStatus.PAID);
        } else if (invoice.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus(Invoice.PaymentStatus.PARTIAL);
        }

        log.info("Payment of {} made on invoice {}", amount, invoice.getInvoiceNumber());
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice addInvoiceItem(UUID invoiceId, InvoiceItemRequest itemReq) {
        Invoice invoice = getInvoiceById(invoiceId);
        InvoiceItem item = new InvoiceItem();
        item.setInvoice(invoice);
        item.setDescription(itemReq.getDescription());
        item.setItemType(itemReq.getItemType());
        item.setQuantity(itemReq.getQuantity());
        item.setUnitPrice(itemReq.getUnitPrice());
        item.setAmount(itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
        invoiceItemRepository.save(item);

        invoice.calculateTotal();
        return invoiceRepository.save(invoice);
    }
}