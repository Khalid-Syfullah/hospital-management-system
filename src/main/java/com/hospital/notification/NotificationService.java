package com.hospital.notification;

import com.hospital.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationOutboxRepository outboxRepository;
    private final UserRepository userRepository;
    private final EmailNotificationService emailNotificationService;

    @Transactional
    public Notification createNotification(UUID userId, String title, String message, Notification.NotificationType type, UUID referenceId, String referenceType) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setReferenceId(referenceId);
        notification.setReferenceType(referenceType);
        notification = notificationRepository.save(notification);

        outboxRepository.save(createOutboxEntry(notification));
        log.info("Notification created for user {}: {}", userId, title);

        return notification;
    }

    private NotificationOutbox createOutboxEntry(Notification notification) {
        NotificationOutbox outbox = new NotificationOutbox();
        outbox.setUserId(notification.getUserId());
        outbox.setTitle(notification.getTitle());
        outbox.setMessage(notification.getMessage());
        outbox.setType(notification.getType());
        outbox.setReferenceId(notification.getReferenceId());
        outbox.setReferenceType(notification.getReferenceType());
        return outbox;
    }

    public Page<Notification> getNotificationsByUser(UUID userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable);
    }

    @Transactional
    public Notification markAsRead(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Async
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void processOutbox() {
        var pendingNotifications = outboxRepository.findBySentAtIsNull();
        for (NotificationOutbox outbox : pendingNotifications) {
            try {
                if (outbox.getUserId() != null) {
                    userRepository.findById(outbox.getUserId()).ifPresent(user -> {
                        emailNotificationService.sendNotificationEmail(user.getEmail(), outbox.getTitle(), outbox.getMessage());
                    });
                }
                outbox.setSentAt(LocalDateTime.now());
                outboxRepository.save(outbox);
                log.info("Outbox notification sent: {}", outbox.getId());
            } catch (Exception e) {
                outbox.setRetryCount(outbox.getRetryCount() + 1);
                outbox.setErrorMessage(e.getMessage());
                outboxRepository.save(outbox);
                log.error("Failed to send notification: {}", outbox.getId(), e);
            }
        }
    }
}