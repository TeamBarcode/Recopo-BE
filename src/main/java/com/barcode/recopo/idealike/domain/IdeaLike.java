package com.barcode.recopo.idealike.domain;

import com.barcode.recopo.idea.domain.Idea;
import com.barcode.recopo.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "idea_like",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_idea_like_member_idea",
                        columnNames = {"member_id", "idea_id"}
                )
        }
)

public class IdeaLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idea_id", nullable = false)
    private Idea idea;

    private IdeaLike(Member member, Idea idea) {
        this.member = member;
        this.idea = idea;
        this.createdAt = LocalDateTime.now();
    }

    public static IdeaLike create(Member member, Idea idea) {
        return new IdeaLike(member, idea);
    }
}
