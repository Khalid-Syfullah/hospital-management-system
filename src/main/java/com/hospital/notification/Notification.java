package com.hospital.notification;

import com.hospital.common.BaseEntity;
import com.hospital.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notif_user_id", columnList = "user_id"),
        @Index(name = "idx_notif_read", columnList = "read_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private NotificationType type;

    @Column
    private LocalDateTime readAt;

    @Column(length = 36)
    private String referenceId;

    public boolean isRead() {
        return readAt != null;
    }
}
