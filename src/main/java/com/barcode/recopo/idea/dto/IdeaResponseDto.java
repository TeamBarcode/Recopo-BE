package com.barcode.recopo.idea.dto;

import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.idea.domain.Idea;

import java.time.LocalDateTime;

public record IdeaResponseDto(
        Long ideaId,
        String title,
        String content,
        String hashtag,
        Category category,
        String visibility,
        LocalDateTime createdAt,
        LocalDateTime updatedAt // 추가
) {
    public static IdeaResponseDto from(Idea idea) {
        return new IdeaResponseDto(
                idea.getIdeaId(),
                idea.getTitle(),
                idea.getContent(),
                idea.getHashtag(),
                idea.getCategory(),
                idea.getVisibility().name(),
                idea.getCreatedAt(),
                idea.getUpdatedAt()
        );
    }
}

