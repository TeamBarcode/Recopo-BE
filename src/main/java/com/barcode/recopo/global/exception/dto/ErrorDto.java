package com.barcode.recopo.global.exception.dto;

public record ErrorDto(
        String timestamp,
        int status,
        String errorCode,
        String message,
        String path
) {
}
