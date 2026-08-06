package com.barcode.recopo.friend.dto.response;

import java.time.LocalDateTime;

public record FriendRequestResponse(
        Long requestId,
        Long requesterId,
        String nickname,
        String profileImageUrl,
        LocalDateTime createdAt
) {
}
