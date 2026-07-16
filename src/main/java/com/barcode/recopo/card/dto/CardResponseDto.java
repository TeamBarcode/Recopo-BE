package com.barcode.recopo.card.dto;

import com.barcode.recopo.card.domain.Category;

import java.time.LocalDateTime;

public record CardResponseDto(
        Long cardId,
        Long memberId,
        String title,
        String content,
        String hashtag,
        Category category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}