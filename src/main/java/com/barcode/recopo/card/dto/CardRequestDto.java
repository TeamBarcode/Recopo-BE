package com.barcode.recopo.card.dto;

import com.barcode.recopo.card.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CardRequestDto(
        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @NotBlank(message = "본문은 필수입니다.")
        String content,

        @NotNull(message = "카테고리는 필수입니다.")
        Category category
) {}