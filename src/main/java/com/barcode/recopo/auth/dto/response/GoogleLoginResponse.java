package com.barcode.recopo.auth.dto.response;

import com.barcode.recopo.member.domain.Member;

public record GoogleLoginResponse(
        Long memberId,
        String accessToken,
        String refreshToken,
        boolean isNewMember,
        boolean profileCompleted
) {
    public static GoogleLoginResponse of(Member member, String accessToken, String refreshToken, boolean isNewMember) {
        return new GoogleLoginResponse(
                member.getMemberId(),
                accessToken,
                refreshToken,
                isNewMember,
                member.isProfileCompleted()
        );
    }
}
