package com.hospital.notification;

import java.util.UUID;

public final class NotificationDtos {
    private NotificationDtos() {
    }

    public record NotificationResponse(UUID id, String type, String message, String recipient, boolean read) {
    }
}
