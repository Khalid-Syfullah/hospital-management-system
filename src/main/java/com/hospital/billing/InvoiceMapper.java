package com.hospital.billing;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper { default InvoiceResponse toResponse(Invoice invoice) { return InvoiceResponse.from(invoice); } }
