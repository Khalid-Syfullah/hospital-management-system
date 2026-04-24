package com.hospital.notification;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final JavaMailSender mailSender;

    public NotificationService(NotificationRepository notificationRepository, OutboxEventRepository outboxEventRepository,
            JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    public void enqueue(String type, String message, String recipient) {
        notificationRepository.save(new Notification(type, message, recipient));
        outboxEventRepository.save(new OutboxEvent(type, message));
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        if (to == null || to.isBlank()) {
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
