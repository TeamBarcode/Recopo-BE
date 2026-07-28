package com.barcode.recopo.card.dto;

import com.barcode.recopo.card.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CardRequestDto(
        String title,
        String content,
        Category category,
        String hashtag
) {}