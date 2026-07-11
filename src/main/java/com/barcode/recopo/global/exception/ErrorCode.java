package com.barcode.recopo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Default
    INTERNAL_SERVER_ERROR(500, "예상치 못한 서버에러가 발생했습니다."),
    BAD_REQUEST(400, "요청 처리에 실패했습니다."),

    // member

    //friend
    CANNOT_REQUEST_SELF(400, "자기 자신에게 친구 신청을 보낼 수 없습니다."),
    FRIEND_REQUEST_ALREADY_EXISTS(409, "이미 대기 중인 친구 신청이 있습니다."),
    ALREADY_FRIENDS(409, "이미 친구인 회원입니다."),
    FRIEND_REQUEST_NOT_FOUND(404, "친구 요청을 찾을 수 없습니다."),
    FRIEND_REQUEST_ALREADY_PROCESSED(409, "이미 처리된 친구 요청입니다."),
    FRIENDSHIP_NOT_FOUND(404, "친구 관계를 찾을 수 없습니다.");

    // 공통 코드 유지
    private final int status;
    private final String message;
}
