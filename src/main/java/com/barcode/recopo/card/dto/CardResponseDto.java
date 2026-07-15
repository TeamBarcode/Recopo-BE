package com.barcode.recopo.card.dto;

import java.time.LocalDateTime;

public record CardResponseDto(
        Long cardId,
        Long memberId,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}