package com.barcode.recopo.idea.domain;

import com.barcode.recopo.card.domain.Card;
import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ideaId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Column(length = 100) // nullable = true (기본값)
    private String hashtag;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(name = "recommendation_id") // nullable = true (기본값)
    private Long recommendationId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 정적 팩토리 메서드
    public static Idea create(Card card) {
        Idea idea = new Idea();
        idea.title = card.getTitle();
        idea.content = card.getContent();
        idea.hashtag = card.getHashtag();
        idea.category = card.getCategory();
        idea.visibility = Visibility.PRIVATE; // 초기 상태는 비공개
        idea.member = card.getMember();
        idea.card = card;
        idea.createdAt = LocalDateTime.now();
        idea.updatedAt = LocalDateTime.now();
        return idea;
    }
}