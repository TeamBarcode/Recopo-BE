package com.barcode.recopo.idealike.dto.response;

public record IdeaLikeResponse(
        Long ideaId,
        boolean liked,
        long likeCount
) {
}
