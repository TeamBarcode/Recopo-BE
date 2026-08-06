package com.barcode.recopo.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProfileSetupRequest(
        @NotBlank(message = "아이디를 입력해주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9_]{2,20}$", message = "영문, 숫자, 언더바만 허용하며 2~20자 이내로 입력해주세요.")
        String loginId,

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(max = 50, message = "닉네임은 50자 이하로 입력해주세요.")
        String nickname
) {
}
