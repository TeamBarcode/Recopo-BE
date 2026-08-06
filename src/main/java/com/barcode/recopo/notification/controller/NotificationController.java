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
@RequestMapping("api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(
                notificationService.getNotifications(memberId)
        );
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> readNotification(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long notificationId
    ) {
        notificationService.readNotification(memberId, notificationId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> readAllNotifications(
            @AuthenticationPrincipal Long memberId
    ){
        notificationService.readAllNotifications(memberId);
        return ResponseEntity.noContent().build();
    }
}
