package com.barcode.recopo.friend.dto.request;

import jakarta.validation.constraints.NotNull;

public record FriendRequestCreateRequest(
        @NotNull(message="친구 신청을 받을 회원 ID는 필수입니다.")
        Long receiverId
) {

}
