package com.barcode.recopo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Default
    INTERNAL_SERVER_ERROR(500, "예상치 못한 서버에러가 발생했습니다."),
    BAD_REQUEST(400, "요청 처리에 실패했습니다."),

    // auth
    INVALID_GOOGLE_TOKEN(401, "유효하지 않은 구글 토큰입니다."),
    INVALID_REFRESH_TOKEN(401, "유효하지 않은 리프레시 토큰입니다."),

    // member
    MEMBER_NOT_FOUND(404, "존재하지 않는 회원입니다."),
    DUPLICATE_LOGIN_ID(409, "이미 사용중인 아이디입니다."),
    INVALID_IMAGE_TYPE(400, "jpg, jpeg, png, webp 형식의 이미지만 업로드할 수 있습니다."),
    IMAGE_TOO_LARGE(400, "이미지 크기는 최대 5MB까지 업로드할 수 있습니다.");

    private final int status;
    private final String message;
}
