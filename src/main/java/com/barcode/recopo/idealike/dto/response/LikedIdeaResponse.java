package com.barcode.recopo.idealike.dto.response;

import java.time.LocalDateTime;

public record LikedIdeaResponse(
    Long ideaId,
    Long memberId,
    String nickname,
    String title,
    boolean liked,
    long likeCount,
    long commentCount,
    LocalDateTime likedAt,
    LocalDateTime createdAt
) {
}
