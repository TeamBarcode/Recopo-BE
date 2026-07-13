package com.barcode.recopo.member.dto.response;

import com.barcode.recopo.member.domain.Member;

public record MemberProfileResponse(
        Long memberId,
        String loginId,
        String nickname,
        String profileImageUrl,
        boolean profileCompleted
) {
    public static MemberProfileResponse from(Member member) {
        return new MemberProfileResponse(
                member.getMemberId(),
                member.getLoginId(),
                member.getNickname(),
                member.getProfileImageUrl(),
                member.isProfileCompleted()
        );
    }
}
