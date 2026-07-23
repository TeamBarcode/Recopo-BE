package com.barcode.recopo.member.dto.request;

import jakarta.validation.constraints.Pattern;

public record ProfileUpdateRequest(
        @Pattern(regexp = "^[a-zA-Z0-9_]{2,20}$", message = "영문, 숫자, 언더바만 허용하며 2~20자 이내로 입력해주세요.")
        String loginId,
        String nickname
) {
}
