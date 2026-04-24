package com.hospital.billing;

import com.hospital.billing.BillingDtos.BillingItemResponse;
import com.hospital.billing.BillingDtos.InvoiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillingMapper {
    @Mapping(target = "patientId", source = "patient.id")
    InvoiceResponse toResponse(Invoice invoice);

    @Mapping(target = "lineTotal", expression = "java(item.lineTotal())")
    BillingItemResponse toResponse(BillingItem item);
}
