package com.barcode.recopo.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record CardColorSettingRequest(
        @NotNull(message = "cardColorEnabled 값은 필수입니다.")
        Boolean cardColorEnabled
) {
}
