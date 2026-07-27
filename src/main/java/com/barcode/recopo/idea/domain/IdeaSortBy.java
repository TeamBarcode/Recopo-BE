package com.barcode.recopo.idea.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IdeaSortBy {
    LATEST("최신생성순"),
    OLDEST("오래된생성순"),
    POPULAR("좋아요순");

    private final String description;
}