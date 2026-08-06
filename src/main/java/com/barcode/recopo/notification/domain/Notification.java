package com.barcode.recopo.notification.domain;

import com.barcode.recopo.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification")

public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String content;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "is_read", nullable = false)
    private boolean read;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    private NotificationTargetType targetType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private Member actor;

    private Notification(
            NotificationType type,
            String content,
            Long targetId,
            NotificationTargetType targetType,
            Member receiver,
            Member actor
    ) {
        this.type = type;
        this.content = content;
        this.targetId = targetId;
        this.targetType = targetType;
        this.receiver = receiver;
        this.actor = actor;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    public static Notification create(
            NotificationType type,
            String content,
            Long targetId,
            NotificationTargetType targetType,
            Member receiver,
            Member actor
    ) {
        return new Notification(
                type,
                content,
                targetId,
                targetType,
                receiver,
                actor
        );
    }

    public void markAsRead() {
        if (!this.read) {
            this.read = true;
            this.readAt = LocalDateTime.now();
        }
    }
}
