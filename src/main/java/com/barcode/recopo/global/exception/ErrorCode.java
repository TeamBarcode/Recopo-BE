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
    DUPLICATE_LOGIN_ID(409, "이미 사용 중인 아이디입니다."),
    INVALID_IMAGE_TYPE(400, "jpg, jpeg, png, webp 형식의 이미지만 업로드할 수 있습니다."),
    IMAGE_TOO_LARGE(400, "이미지 크기는 최대 5MB까지 업로드할 수 있습니다."),
    EMPTY_PROFILE_UPDATE_REQUEST(400, "수정할 항목을 하나 이상 입력해주세요."),

    // card
    CARD_NOT_FOUND(404, "해당 카드를 찾을 수 없습니다."),
    UNAUTHORIZED_CARD_ACCESS(403, "본인의 카드만 조회할 수 있습니다."),
    TOO_MANY_HASHTAGS(400, "해시태그는 최대 5개까지만 등록할 수 있습니다."),
    TITLE_REQUIRED(400, "제목은 필수입니다."),
    CONTENT_REQUIRED(400, "본문은 필수입니다."),
    CATEGORY_REQUIRED(400, "카테고리는 필수입니다."),

    //recommendation
    RECOMMENDATION_NOT_FOUND(404, "AI 추천 결과를 찾을 수 없습니다."),

    // idea
    IDEA_NOT_FOUND(404, "해당 아이디어를 찾을 수 없습니다."),
    UNAUTHORIZED_IDEA_ACCESS(403, "본인의 아이디어만 접근할 수 있습니다."),
    ALREADY_CONVERTED_CARD(400, "이미 아이디어로 전환된 카드입니다."),
    CANNOT_DELETE_CONVERTED_CARD(400, "아이디어로 전환된 카드는 삭제할 수 없습니다."),
    CANNOT_LIKE_OWN_IDEA(400, "본인의 아이디어에는 좋아요를 누를 수 없습니다."),
    IDEA_LIKE_ALREADY_EXISTS(409, "이미 좋아요를 누른 아이디어입니다."),
    IDEA_LIKE_NOT_FOUND(404, "좋아요 내역을 찾을 수 없습니다."),

    // friend
    CANNOT_REQUEST_SELF(400, "자기 자신에게 친구 신청을 보낼 수 없습니다."),
    FRIEND_REQUEST_ALREADY_EXISTS(409, "이미 대기 중인 친구 신청이 있습니다."),
    ALREADY_FRIENDS(409, "이미 친구인 회원입니다."),
    FRIEND_REQUEST_NOT_FOUND(404, "친구 요청을 찾을 수 없습니다."),
    FRIEND_REQUEST_ALREADY_PROCESSED(409, "이미 처리된 친구 요청입니다."),
    FRIENDSHIP_NOT_FOUND(404, "친구 관계를 찾을 수 없습니다."),

    // comment
    COMMENT_NOT_FOUND(404, "댓글을 찾을 수 없습니다."),
    UNAUTHORIZED_COMMENT_ACCESS(403, "본인이 작성한 댓글만 수정하거나 삭제할 수 있습니다."),

    //Notification
    NOTIFICATION_NOT_FOUND(404, "알림을 찾을 수 없습니다.");

    // 공통 코드 유지
    private final int status;
    private final String message;
}
