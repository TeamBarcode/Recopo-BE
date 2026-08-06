package com.barcode.recopo.idea.domain;

import com.barcode.recopo.card.domain.Card;
import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.member.domain.Member;
import com.barcode.recopo.recommendation.domain.Recommendation;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;

    @Column(nullable = false)
    private int likeCount = 0;
    private LocalDateTime createdAt;

    // 정적 팩토리 메서드
    public static Idea create(Card card, Visibility visibility) {
        Idea idea = new Idea();
        idea.title = card.getTitle();
        idea.content = card.getContent();
        idea.hashtag = card.getHashtag();
        idea.category = card.getCategory();
        idea.visibility = visibility;
        idea.member = card.getMember();
        idea.card = card;
        idea.likeCount = 0;
        idea.createdAt = LocalDateTime.now();
        return idea;
    }

    public static Idea createWithRecommendation(Card card, Visibility visibility, Recommendation recommendation) {
        Idea idea = new Idea();
        idea.title = card.getTitle();
        idea.content = card.getContent();
        idea.hashtag = card.getHashtag();
        idea.category = card.getCategory();
        idea.visibility = visibility;
        idea.member = card.getMember();
        idea.card = card;
        idea.recommendation = recommendation; // 추천 결과 매핑!
        idea.likeCount = 0;
        idea.createdAt = LocalDateTime.now();
        return idea;
    }

    public void update(String title, String hashtag, Category category, Visibility visibility) {
        if (title != null) this.title = title;
        if (hashtag != null) this.hashtag = hashtag;
        if (category != null) this.category = category;
        if (visibility != null) this.visibility = visibility;
    }
}