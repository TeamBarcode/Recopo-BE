package com.barcode.recopo.notification.controller;

import com.barcode.recopo.notification.dto.response.NotificationResponse;
import com.barcode.recopo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    //내 알림 목록 조회
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(
                notificationService.getNotifications(memberId)
        );
    }

    //알림 한 개 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> readNotification(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long notificationId
    ) {
        notificationService.readNotification(memberId, notificationId);
        return ResponseEntity.noContent().build();
    }

    //모든 알림 읽음 처리
    @PatchMapping("/read-all")
    public ResponseEntity<Void> readAllNotifications(
            @AuthenticationPrincipal Long memberId
    ){
        notificationService.readAllNotifications(memberId);
        return ResponseEntity.noContent().build();
    }
}
