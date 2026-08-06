package com.barcode.recopo.recommendation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class RecommendationResponseDto {

    private Long cardId;
    private RepositoryRecommendation recommendation;

    @Getter
    @NoArgsConstructor
    public static class RepositoryRecommendation {
        private Long repositoryId;
        private String name;
        private String fullName;
        private String url;
        private String description;
        private String language;
        private List<String> techStack;
        private int stars;
        private int forks;
        private String updatedAt;
        private String reason;
    }
}