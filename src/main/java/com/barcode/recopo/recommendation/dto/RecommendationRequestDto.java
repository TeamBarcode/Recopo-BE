package com.barcode.recopo.recommendation.dto;

import java.util.List;

public class RecommendationRequestDto {

    public record Create(
            Long cardId,
            List<Long> excludedRepositoryIds // 재요청 시 제외할 레포지토리 ID 목록
    ) {}
}