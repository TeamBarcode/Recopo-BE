package com.barcode.recopo.idea.dto;

import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.idea.domain.Visibility;
import java.util.List;

public class IdeaRequestDto {

    public record Save(
            Long cardId,
            Visibility visibility,
            Long recommendationId
    ) {}

    public record Update(
            String title,
            String hashtag,
            Category category,
            Visibility visibility
    ) {}
}