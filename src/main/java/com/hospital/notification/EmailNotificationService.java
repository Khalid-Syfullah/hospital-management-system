package com.hospital.notification;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {
    private final JavaMailSender mailSender;
    public EmailNotificationService(JavaMailSender mailSender) { this.mailSender = mailSender; }
    @Async
    @Retryable
    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to); message.setSubject(subject); message.setText(body);
        mailSender.send(message);
    }
}
