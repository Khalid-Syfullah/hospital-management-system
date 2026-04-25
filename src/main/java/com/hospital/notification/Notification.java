package com.hospital.notification;

import com.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {
    public enum Type { IN_APP, EMAIL }
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Type type = Type.IN_APP;
    @Column(nullable = false) private String recipient;
    @Column(nullable = false) private String subject;
    @Column(nullable = false, length = 4000) private String body;
    private Instant readAt;
    public Type getType() { return type; } public void setType(Type type) { this.type = type; }
    public String getRecipient() { return recipient; } public void setRecipient(String recipient) { this.recipient = recipient; }
    public String getSubject() { return subject; } public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; } public void setBody(String body) { this.body = body; }
    public Instant getReadAt() { return readAt; } public void setReadAt(Instant readAt) { this.readAt = readAt; }
}
