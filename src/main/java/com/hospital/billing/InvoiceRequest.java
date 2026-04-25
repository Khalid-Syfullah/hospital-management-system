package com.hospital.billing;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record InvoiceRequest(@NotNull UUID patientId, String insuranceClaimNumber, @Valid @NotEmpty List<InvoiceItemRequest> items) {}
