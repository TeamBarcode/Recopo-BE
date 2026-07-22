package com.barcode.recopo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
    IMAGE_TOO_LARGE(400, "이미지 크기는 최대 5MB까지 업로드할 수 있습니다."),

    // card
    CARD_NOT_FOUND(404, "해당 카드를 찾을 수 없습니다."),
    UNAUTHORIZED_CARD_ACCESS(403, "본인의 카드만 조회할 수 있습니다."),

    // idea
    IDEA_NOT_FOUND(404, "해당 아이디어를 찾을 수 없습니다."),
    UNAUTHORIZED_IDEA_ACCESS(403, "본인의 아이디어만 접근할 수 있습니다."),
    ALREADY_CONVERTED_CARD(400, "이미 아이디어로 전환된 카드입니다."),
    CANNOT_DELETE_CONVERTED_CARD(400, "아이디어로 전환된 카드는 삭제할 수 없습니다."),

    // friend
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
