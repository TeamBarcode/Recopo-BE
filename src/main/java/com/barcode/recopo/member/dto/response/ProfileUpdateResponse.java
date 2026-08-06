package com.barcode.recopo.member.dto.response;

import com.barcode.recopo.member.domain.Member;

public record ProfileUpdateResponse(
        Long memberId,
        String loginId,
        String nickname,
        boolean profileCompleted
) {
    public static ProfileUpdateResponse from(Member member) {
        return new ProfileUpdateResponse(
                member.getMemberId(),
                member.getLoginId(),
                member.getNickname(),
                member.isProfileCompleted()
        );
    }
}
