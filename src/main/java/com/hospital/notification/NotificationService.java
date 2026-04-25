package com.hospital.notification;

import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {
    private final NotificationRepository notifications;
    private final NotificationOutboxRepository outbox;
    public NotificationService(NotificationRepository notifications, NotificationOutboxRepository outbox) {
        this.notifications = notifications; this.outbox = outbox;
    }
    @Transactional public void queue(String eventType, String subject, String payload) {
        NotificationOutbox item = new NotificationOutbox();
        item.setEventType(eventType); item.setSubject(subject); item.setPayload(payload);
        outbox.save(item);
    }
    @Transactional public NotificationResponse create(String recipient, String subject, String body) {
        Notification n = new Notification(); n.setRecipient(recipient); n.setSubject(subject); n.setBody(body);
        return NotificationResponse.from(notifications.save(n));
    }
    @Transactional(readOnly = true) public Page<NotificationResponse> list(Pageable pageable) { return notifications.findAll(pageable).map(NotificationResponse::from); }
    @Transactional public void processOutbox() {
        outbox.findTop50ByProcessedFalseOrderByCreatedAtAsc().forEach(item -> {
            item.setProcessed(true); item.setProcessedAt(Instant.now());
        });
    }
}
