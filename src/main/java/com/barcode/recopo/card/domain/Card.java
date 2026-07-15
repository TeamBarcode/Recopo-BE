package com.barcode.recopo.card.domain;

import com.barcode.recopo.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long cardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(length = 100)
    private String hashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private Card(String title, String content, Category category, String hashtag, Member member) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.member = member;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Card create(String title, String content, Category category, String hashtag, Member member) {
        return new Card(title, content, category, hashtag, member);
    }

}