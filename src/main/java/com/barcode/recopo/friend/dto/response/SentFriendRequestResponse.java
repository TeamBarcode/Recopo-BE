package com.barcode.recopo.friend.dto.response;

import java.time.LocalDateTime;

public record SentFriendRequestResponse(
        Long requestId,
        Long receiverId,
        String receiverNickname,
        String receiverProfileImageUrl,
        LocalDateTime createdAt
) {
}
