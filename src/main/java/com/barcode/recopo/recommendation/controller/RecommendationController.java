package com.barcode.recopo.recommendation.controller;

import com.barcode.recopo.recommendation.domain.Recommendation;
import com.barcode.recopo.recommendation.dto.RecommendationRequestDto;
import com.barcode.recopo.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    // AI 추천 요청 (재요청 시 excludedRepositoryIds 포함)
    @PostMapping("/request")
    public ResponseEntity<List<Recommendation>> requestRecommendation(@RequestBody RecommendationRequestDto.Create requestDto) {
        List<Recommendation> recommendations = recommendationService.requestRecommendation(requestDto);
        return ResponseEntity.ok(recommendations);
    }

    // 추천 취소 (saveHistory 파라미터로 보관 여부 결정: true=이력 남김, false=삭제)
    @PatchMapping("/{recommendationId}/cancel")
    public ResponseEntity<Void> cancelRecommendation(
            @PathVariable Long recommendationId,
            @RequestParam boolean saveHistory
    ) {
        recommendationService.cancelRecommendation(recommendationId, saveHistory);
        return ResponseEntity.ok().build();
    }

    // 특정 카드의 추천 이력
    @GetMapping("/cards/{cardId}")
    public ResponseEntity<List<Recommendation>> getRecommendations(
            @PathVariable Long cardId,
            @RequestParam(required = false, defaultValue = "LATEST") String sortBy
    ) {
        List<Recommendation> recommendations = recommendationService.getSortedRecommendations(cardId, sortBy);
        return ResponseEntity.ok(recommendations);
    }
}