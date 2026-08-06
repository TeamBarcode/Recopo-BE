package com.barcode.recopo.idea.dto;

import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.idea.domain.Idea;
import com.barcode.recopo.recommendation.domain.Recommendation;

import java.time.LocalDateTime;
import java.util.List;

public record IdeaResponseDto(
        Long ideaId,
        String title,
        String content,
        String hashtag,
        Category category,
        String visibility,
        int likeCount,
        LocalDateTime createdAt,
        RecommendationResponse recommendation
) {
    public static IdeaResponseDto from(Idea idea) {
        return new IdeaResponseDto(
                idea.getIdeaId(),
                idea.getTitle(),
                idea.getContent(),
                idea.getHashtag(),
                idea.getCategory(),
                idea.getVisibility().name(),
                idea.getLikeCount(),
                idea.getCreatedAt(),
                // 추천 정보가 존재하면 변환해서 담고, 없으면 null 처리
                idea.getRecommendation() != null ? RecommendationResponse.from(idea.getRecommendation()) : null
        );
    }

    // 추천 정보 응답용 내부 레코드 (또는 별도 클래스로 분리해도 좋습니다)
    public record RecommendationResponse(
            Long recommendationId,
            Long repositoryId,
            String repositoryName,
            String repositoryFullName,
            String repositoryUrl,
            String repositoryDescription,
            String language,
            String techStack,
            int starCount,
            int forkCount,
            String updatedAt,
            String reason
    ) {
        public static RecommendationResponse from(Recommendation rec) {
            return new RecommendationResponse(
                    rec.getRecommendationId(),
                    rec.getRepositoryId(),
                    rec.getRepositoryName(),
                    rec.getRepositoryFullName(),
                    rec.getRepositoryUrl(),
                    rec.getRepositoryDescription(),
                    rec.getLanguage(),
                    rec.getTechStack(),
                    rec.getStarCount(),
                    rec.getForkCount(),
                    rec.getUpdatedAt(),
                    rec.getReason()
            );
        }
    }
}