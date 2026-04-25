package com.hospital.ward;

import jakarta.validation.constraints.NotBlank;

public record WardRequest(@NotBlank String name, String floor) {}
