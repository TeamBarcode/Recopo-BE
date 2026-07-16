package com.barcode.recopo.friend.controller;

import com.barcode.recopo.friend.dto.request.FriendRequestCreateRequest;
import com.barcode.recopo.friend.dto.response.FriendRequestResponse;
import com.barcode.recopo.friend.dto.response.FriendResponse;
import com.barcode.recopo.friend.service.FriendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/requests")
    public ResponseEntity<Void> sendFriendRequest(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody FriendRequestCreateRequest request
    ) {
        friendService.sendFriendRequest(
                memberId,
                request.receiverId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/requests/received")
    public ResponseEntity<List<FriendRequestResponse>>
    getReceivedRequests(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(
                friendService.getReceivedRequests(memberId)
        );
    }

    @PatchMapping("/requests/{requestId}/accept")
    public ResponseEntity<Void> acceptFriendRequest(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long requestId
    ) {
        friendService.acceptFriendRequest(memberId, requestId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/requests/{requestId}/reject")
    public ResponseEntity<Void> rejectFriendRequest(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long requestId
    ) {
        friendService.rejectFriendRequest(memberId, requestId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FriendResponse>> getFriends(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(
                friendService.getFriends(memberId)
        );
    }

}
