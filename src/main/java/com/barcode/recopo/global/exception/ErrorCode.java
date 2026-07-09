package com.barcode.recopo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Default
    INTERNAL_SERVER_ERROR(500, "예상치 못한 서버에러가 발생했습니다."),
    BAD_REQUEST(400, "요청 처리에 실패했습니다.");

    // member

    private final int status;
    private final String message;
}
