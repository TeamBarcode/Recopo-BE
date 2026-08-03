package com.barcode.recopo.recommendation.service;

import com.barcode.recopo.card.domain.Card;
import com.barcode.recopo.card.repository.CardRepository;
import com.barcode.recopo.global.exception.CustomException;
import com.barcode.recopo.global.exception.ErrorCode;
import com.barcode.recopo.recommendation.domain.Recommendation;
import com.barcode.recopo.recommendation.dto.RecommendationRequestDto;
import com.barcode.recopo.recommendation.dto.RecommendationResponseDto;
import com.barcode.recopo.recommendation.repository.RecommendationRepository;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final CardRepository cardRepository;
    private final WebClient aiServerWebClient;
    @Transactional
    public List<Recommendation> requestRecommendation(RecommendationRequestDto.Create requestDto) {

        // 1. cardId로 DB에서 실제 카드 정보를 안전하게 조회하여 데이터 정합성 보장
        Card card = cardRepository.findById(requestDto.cardId())
                .orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_FOUND));

        Map<String, Object> aiRequestBody = Map.of(
                "cardId", card.getCardId(),
                "title", card.getTitle(),
                "content", card.getContent(),
                "excludedRepositoryIds", requestDto.excludedRepositoryIds() != null ? requestDto.excludedRepositoryIds() : List.of()
        );

        // 2. AI 서버로 POST 요청 전송 및 응답(RecommendationResponse) 받기
        RecommendationResponseDto aiResponse = aiServerWebClient.post()
                .uri("/api/recommendations")
                .bodyValue(aiRequestBody)
                .retrieve()
                .bodyToMono(RecommendationResponseDto.class)
                .block(); // 동기식으로 결과 대기

        List<Recommendation> savedRecommendations = new java.util.ArrayList<>();

        // 3. AI 서버가 준 추천 결과가 존재할 경우 DB에 저장
        if (aiResponse != null && aiResponse.getRecommendation() != null) {
            RecommendationResponseDto.RepositoryRecommendation rec = aiResponse.getRecommendation();

            // techStack 리스트를 콤마(,)로 구분된 문자열로 변환
            String techStackStr = (rec.getTechStack() != null) ? String.join(", ", rec.getTechStack()) : "";

            Recommendation recommendation = Recommendation.create(
                    aiResponse.getCardId(),
                    rec.getRepositoryId(),
                    rec.getName(),
                    rec.getFullName(),
                    rec.getUrl(),
                    rec.getDescription(),
                    rec.getLanguage(),
                    techStackStr,
                    rec.getStars(),
                    rec.getForks(),
                    rec.getUpdatedAt(),
                    rec.getReason()
            );

            savedRecommendations.add(recommendationRepository.save(recommendation));
        }
        return savedRecommendations;
    }

    /**
     * 추천 취소 처리
     * @param saveHistory true면 상태를 CANCELED로 변경하여 이력 보관, false면 완전 삭제
     */
    @Transactional
    public void cancelRecommendation(Long recommendationId, boolean saveHistory) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECOMMENDATION_NOT_FOUND));

        if (saveHistory) {
            recommendation.cancel(); // 이력 보관
        } else {
            recommendationRepository.delete(recommendation); // 완전 삭제
        }
    }

    public List<Recommendation> getSortedRecommendations(Long cardId, String sortBy) {
        String sortCriteria = (sortBy != null) ? sortBy.toUpperCase() : "LATEST";

        Sort sort;
        switch (sortCriteria) {
            case "STAR":
                sort = Sort.by(Sort.Direction.DESC, "starCount");
                break;
            case "FORK":
                sort = Sort.by(Sort.Direction.ASC, "forkCount");
                break;
            case "LATEST":
            default:
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
        }
        return recommendationRepository.findAllByCardId(cardId, sort);
    }
}