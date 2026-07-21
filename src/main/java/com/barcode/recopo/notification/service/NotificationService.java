package com.barcode.recopo.notification.service;

import com.barcode.recopo.global.exception.CustomException;
import com.barcode.recopo.global.exception.ErrorCode;
import com.barcode.recopo.member.domain.Member;
import com.barcode.recopo.member.repository.MemberRepository;
import com.barcode.recopo.notification.dto.response.NotificationResponse;
import com.barcode.recopo.notification.repository.NotificationRepository;
import com.barcode.recopo.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public void readNotification(Long memberId, Long notificationId) {
        Member receiver = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Notification notification = notificationRepository
                .findByNotificationIdAndReceiver(notificationId, receiver)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        notification.markAsRead();
    }
    public void readAllNotifications(Long memberId) {
        Member receiver = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Notification> unreadNotifications =
                notificationRepository.findAllByReceiverAndReadFalse(receiver);

        unreadNotifications.forEach(Notification::markAsRead);
    }

    public List<NotificationResponse> getNotifications(Long memberId){
        Member receiver=memberRepository.findById(memberId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        return notificationRepository
                .findAllByReceiverOrderByCreatedAtDesc(receiver)
                .stream()
                .map(notification->new NotificationResponse(
                        notification.getNotificationId(),
                        notification.getType(),
                        notification.getContent(),
                        notification.getTargetId(),
                        notification.getTargetType(),
                        notification.getActor().getMemberId(),
                        notification.getActor().getNickname(),
                        notification.getActor().getProfileImageUrl(),
                        notification.isRead(),
                        notification.getCreatedAt(),
                        notification.getReadAt()
                )).toList();
    }
}
