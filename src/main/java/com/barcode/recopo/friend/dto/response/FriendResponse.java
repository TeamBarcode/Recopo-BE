package com.barcode.recopo.friend.dto.response;

public record FriendResponse(
        Long memberId,
        String nickname,
        String profileImageUrl
) {
}
