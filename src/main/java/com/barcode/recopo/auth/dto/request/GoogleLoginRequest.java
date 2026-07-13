package com.barcode.recopo.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GoogleLoginRequest(
        @NotBlank(message = "idToken을 입력해주세요.")
        String idToken
) {
}
