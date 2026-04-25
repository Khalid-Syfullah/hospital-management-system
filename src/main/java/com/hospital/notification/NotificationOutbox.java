package com.hospital.notification;

import com.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_outbox")
@Getter
@Setter
public class NotificationOutbox extends BaseEntity {

    @Column(name = "user_id")
    private UUID userId;

    private String title;

    @Lob
    private String message;

    @Enumerated(EnumType.STRING)
    private Notification.NotificationType type = Notification.NotificationType.INFO;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "retry_count")
    private int retryCount = 0;

    private String errorMessage;
}