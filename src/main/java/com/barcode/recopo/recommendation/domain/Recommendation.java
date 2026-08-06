package com.barcode.recopo.recommendation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    private Long recommendationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // COMPLETED, CANCELED 등

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "repository_id")
    private Long repositoryId; // 추가: 깃허브 레포 고유 ID

    @Column(name = "repository_name", nullable = false)
    private String repositoryName;

    @Column(name = "repository_full_name")
    private String repositoryFullName; // 추가: owner/repo 형태의 풀네임 (예: owner/pose-estimation-web)

    @Column(name = "repository_url", nullable = false)
    private String repositoryUrl;

    @Column(name = "repository_description", columnDefinition = "TEXT", nullable = false)
    private String repositoryDescription;

    @Column(name = "language")
    private String language; // 추가: 주요 언어 (예: Python)

    @Column(name = "tech_stack")
    private String techStack; // 추가: 기술 스택 (리스트를 콤마 문자열로 저장)

    @Column(name = "star_count", nullable = false)
    private int starCount;

    @Column(name = "fork_count", nullable = false)
    private int forkCount;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason; // 추가: AI 추천 이유

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    public static Recommendation create(
            Long cardId, Long repositoryId, String repositoryName, String repositoryFullName,
            String repositoryUrl, String repositoryDescription, String language,
            String techStack, int starCount, int forkCount, String updatedAt, String reason
    ) {
        Recommendation recommendation = new Recommendation();
        recommendation.cardId = cardId;
        recommendation.repositoryId = repositoryId;
        recommendation.repositoryName = repositoryName;
        recommendation.repositoryFullName = repositoryFullName;
        recommendation.repositoryUrl = repositoryUrl;
        recommendation.repositoryDescription = repositoryDescription;
        recommendation.language = language;
        recommendation.techStack = techStack;
        recommendation.starCount = starCount;
        recommendation.forkCount = forkCount;
        recommendation.updatedAt = updatedAt;
        recommendation.reason = reason;
        recommendation.status = Status.COMPLETED; // 기본 생성 시 완료 상태
        recommendation.createdAt = LocalDateTime.now();
        return recommendation;
    }

    // 취소 상태로 변경하는 메서드 (이력 보관 시 사용)
    public void cancel() {
        this.status = Status.CANCELED;
    }

    public enum Status {
        COMPLETED, CANCELED
    }
}