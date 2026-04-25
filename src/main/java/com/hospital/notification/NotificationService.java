package com.hospital.notification;

import com.hospital.appointment.Appointment;
import com.hospital.user.User;
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
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationOutboxRepository outboxRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Async
    @Transactional
    public void sendAppointmentConfirmation(Appointment appointment) {
        User patient = appointment.getPatient().getEmail() != null
                ? userRepository.findByEmailAndDeletedAtIsNull(appointment.getPatient().getEmail()).orElse(null)
                : null;

        String title = "Appointment Confirmed";
        String message = String.format("Your appointment with Dr. %s is scheduled for %s.",
                appointment.getDoctor().getUser().getFullName(),
                appointment.getStartTime());

        if (patient != null) {
            saveNotification(patient, title, message, NotificationType.APPOINTMENT_CONFIRMATION,
                    appointment.getId().toString());
        }

        if (appointment.getPatient().getEmail() != null) {
            queueEmail(appointment.getPatient().getEmail(), title, message);
        }
    }

    @Transactional
    public void saveNotification(User user, String title, String message, NotificationType type, String referenceId) {
        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .referenceId(referenceId)
                .build();
        notificationRepository.save(notification);
    }

    @Transactional
    public void queueEmail(String recipient, String subject, String body) {
        NotificationOutbox outbox = NotificationOutbox.builder()
                .recipient(recipient)
                .subject(subject)
                .body(body)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .attempts(0)
                .build();
        outboxRepository.save(outbox);
    }

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void processOutbox() {
        List<NotificationOutbox> pending = outboxRepository.findByStatusAndAttemptsLessThan("PENDING", 3);
        for (NotificationOutbox item : pending) {
            try {
                emailService.sendEmail(item.getRecipient(), item.getSubject(), item.getBody());
                item.setStatus("SENT");
                item.setProcessedAt(LocalDateTime.now());
                log.info("Outbox email sent to {}", item.getRecipient());
            } catch (Exception e) {
                item.setAttempts(item.getAttempts() + 1);
                item.setErrorMessage(e.getMessage());
                if (item.getAttempts() >= 3) {
                    item.setStatus("FAILED");
                }
                log.warn("Outbox email failed for {}: {}", item.getRecipient(), e.getMessage());
            }
            outboxRepository.save(item);
        }
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsForUser(UUID userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::toResponse);
    }

    @Transactional
    public void markAsRead(UUID notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            if (n.getReadAt() == null) {
                n.setReadAt(LocalDateTime.now());
                notificationRepository.save(n);
            }
        });
    }

    @Transactional(readOnly = true)
    public long countUnread(UUID userId) {
        return notificationRepository.countByUserIdAndReadAtIsNull(userId);
    }

    private NotificationResponse toResponse(Notification n) {
        NotificationResponse resp = new NotificationResponse();
        resp.setId(n.getId());
        resp.setTitle(n.getTitle());
        resp.setMessage(n.getMessage());
        resp.setType(n.getType());
        resp.setRead(n.isRead());
        resp.setReferenceId(n.getReferenceId());
        resp.setCreatedAt(n.getCreatedAt());
        resp.setReadAt(n.getReadAt());
        return resp;
    }
}
