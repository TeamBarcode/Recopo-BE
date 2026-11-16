package com.barcode.recopo.member.dto.response;

public record MemberSearchResponse (
        Long memberId,
        String nickname,
        String profileImageUrl
){

}