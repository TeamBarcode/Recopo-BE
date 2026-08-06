package com.barcode.recopo.comment.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        Long memberId,
        String nickname,
        String profileImageUrl,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
