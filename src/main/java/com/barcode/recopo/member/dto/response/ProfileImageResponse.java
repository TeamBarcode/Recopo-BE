package com.barcode.recopo.member.dto.response;

import com.barcode.recopo.member.domain.Member;

import java.time.LocalDateTime;

public record ProfileImageResponse(
        Long memberId,
        String profileImageUrl,
        LocalDateTime updatedAt
) {
    public static ProfileImageResponse from(Member member) {
        return new ProfileImageResponse(
                member.getMemberId(),
                member.getProfileImageUrl(),
                member.getUpdatedAt()
        );
    }
}
