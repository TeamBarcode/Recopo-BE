package com.barcode.recopo.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
        @NotBlank(message = "refreshToken을 입력해주세요.")
        String refreshToken
) {
}
