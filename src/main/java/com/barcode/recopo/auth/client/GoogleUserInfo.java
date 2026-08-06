package com.barcode.recopo.auth.client;

public record GoogleUserInfo(
        String sub,
        String email,
        String picture
) {
}
