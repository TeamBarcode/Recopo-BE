package com.barcode.recopo.member.dto.response;

import com.barcode.recopo.member.domain.Member;

import java.time.LocalDateTime;

public record MemberProfileResponse(
        Long memberId,
        String loginId,
        String email,
        String nickname,
        String profileImageUrl,
        boolean profileCompleted,
        LocalDateTime createdAt,
        long cardCount,
        long ideaCount,
        boolean cardColorEnabled
) {
    public static MemberProfileResponse from(Member member, long cardCount, long ideaCount) {
        return new MemberProfileResponse(
                member.getMemberId(),
                member.getLoginId(),
                member.getEmail(),
                member.getNickname(),
                member.getProfileImageUrl(),
                member.isProfileCompleted(),
                member.getCreatedAt(),
                cardCount,
                ideaCount,
                member.isCardColorEnabled()
        );
    }
}
