package com.barcode.recopo.card.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardSortBy {
    LATEST("최신생성순"),
    OLDEST("오래된생성순"),
    MODIFIED("최근수정순");

    private final String description;
}