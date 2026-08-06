package com.barcode.recopo.notification.dto.response;

import com.barcode.recopo.notification.domain.NotificationTargetType;
import com.barcode.recopo.notification.domain.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long notificationId,
        NotificationType type,
        String content,
        Long targetId,
        NotificationTargetType targetType,
        Long actorId,
        String actorNickname,
        String actorProfileImageUrl,
        boolean read,
        LocalDateTime createdAt,
        LocalDateTime readAt
) {
}
