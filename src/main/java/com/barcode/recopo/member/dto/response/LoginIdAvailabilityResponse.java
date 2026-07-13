package com.barcode.recopo.member.dto.response;

public record LoginIdAvailabilityResponse(
        String loginId,
        boolean available
) {
}
