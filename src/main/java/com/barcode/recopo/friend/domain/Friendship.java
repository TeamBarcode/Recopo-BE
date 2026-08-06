package com.barcode.recopo.friend.domain;

import com.barcode.recopo.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Table(name="friendship")

public class Friendship {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="friendship_id")
    private Long friendshipId;

    @Column(name="created_at", nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="friend_id", nullable=false)
    private Member friend;

    private Friendship(Member member, Member friend){
        this.member=member;
        this.friend=friend;
        this.createdAt=LocalDateTime.now();
    }

    public static Friendship create(Member member, Member friend){
        return new Friendship(member, friend);
    }

}
