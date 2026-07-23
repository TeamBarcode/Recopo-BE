package com.barcode.recopo.notification.repository;

import com.barcode.recopo.member.domain.Member;
import com.barcode.recopo.notification.domain.Notification;
import com.barcode.recopo.notification.domain.NotificationTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiverOrderByCreatedAtDesc(
            Member receiver
    );
    Optional<Notification> findByNotificationIdAndReceiver(
            Long notificationId,
            Member receiver
    );
    List<Notification> findAllByReceiverAndReadFalse(
            Member receiver
    );
    void deleteByReceiverAndTargetTypeAndTargetId(
            Member receiver,
            NotificationTargetType targetType,
            Long targetId
    );

}
