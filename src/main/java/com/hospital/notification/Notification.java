package com.hospital.notification;

import com.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {
    @Column(nullable = false)
    private String type;
    @Column(nullable = false, length = 2000)
    private String message;
    private String recipient;
    @Column(nullable = false)
    private boolean read = false;

    protected Notification() {
    }

    public Notification(String type, String message, String recipient) {
        this.type = type;
        this.message = message;
        this.recipient = recipient;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getRecipient() {
        return recipient;
    }

    public boolean isRead() {
        return read;
    }

    public void markRead() {
        read = true;
    }
}
