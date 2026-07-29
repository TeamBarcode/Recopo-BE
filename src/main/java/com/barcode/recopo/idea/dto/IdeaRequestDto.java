package com.barcode.recopo.idea.dto;

import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.idea.domain.Visibility;

public class IdeaRequestDto {

    public record Save(
            Long cardId,
            Visibility visibility // 👈 공개/비공개 여부 추가
    ) {}

    public record Update(
            String title,
            String hashtag,
            Category category,
            Visibility visibility
    ) {}
}