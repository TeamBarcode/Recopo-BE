package com.barcode.recopo.card.dto;

import java.time.LocalDateTime;

public record CardResponseDto(
        Long cardId,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}