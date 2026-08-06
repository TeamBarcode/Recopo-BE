package com.barcode.recopo.card.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    EDUCATION("교육"),
    WORK_PRODUCTIVITY("업무/생산성"),
    GAME_MEDIA("게임/미디어"),
    SOCIAL("소셜"),
    DEVELOPMENT_TOOLS("개발 툴"),
    ETC("기타");

    private final String description;
}