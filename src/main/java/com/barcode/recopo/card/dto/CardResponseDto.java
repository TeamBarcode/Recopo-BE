package com.barcode.recopo.card.dto;

import java.time.LocalDateTime;

public record CardResponseDto(
        Long card_id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}