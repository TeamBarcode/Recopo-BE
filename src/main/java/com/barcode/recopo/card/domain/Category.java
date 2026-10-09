package com.barcode.recopo.card.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    CONTENT_MEDIA("콘텐츠/미디어"),
    LIFE("생활"),
    HEALTH("건강"),
    WORK_TOOLS("업무/도구"),
    DEVELOPMENT_DESIGN("개발/디자인"),
    PEOPLE("사람"),
    ETC("기타");

    private final String description;
}