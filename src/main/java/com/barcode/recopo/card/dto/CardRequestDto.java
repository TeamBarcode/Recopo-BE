package com.barcode.recopo.card.dto;

import java.util.List;

public record CardRequestDto(
        String title,
        String content,
        List<String> hashtags
) {}