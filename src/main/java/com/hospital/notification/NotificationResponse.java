package com.hospital.notification;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(UUID id, Notification.Type type, String recipient, String subject, String body, Instant readAt) {
    static NotificationResponse from(Notification n) { return new NotificationResponse(n.getId(), n.getType(), n.getRecipient(), n.getSubject(), n.getBody(), n.getReadAt()); }
}
