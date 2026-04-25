package com.hospital.billing;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillingMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java(invoice.getPatient().getFirstName() + ' ' + invoice.getPatient().getLastName())")
    @Mapping(target = "patientMrn", source = "patient.mrn")
    InvoiceResponse toResponse(Invoice invoice);

    BillingItemResponse toItemResponse(BillingItem item);
}
