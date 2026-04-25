package com.hospital.pharmacy;

import jakarta.validation.constraints.Min;

public record DispenseRequest(@Min(1) int quantity) {}
