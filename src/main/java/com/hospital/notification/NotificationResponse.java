package com.hospital.notification;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationResponse {
    private UUID id;
    private String title;
    private String message;
    private NotificationType type;
    private boolean read;
    private String referenceId;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}
