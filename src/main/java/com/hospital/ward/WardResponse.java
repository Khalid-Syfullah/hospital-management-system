package com.hospital.ward;

import java.util.UUID;

public record WardResponse(UUID id, String name, String floor) {
    static WardResponse from(Ward w) { return new WardResponse(w.getId(), w.getName(), w.getFloor()); }
}
