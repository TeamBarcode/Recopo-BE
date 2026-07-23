package com.barcode.recopo.auth.controller;

import com.barcode.recopo.auth.dto.request.GoogleLoginRequest;
import com.barcode.recopo.auth.dto.request.TokenRefreshRequest;
import com.barcode.recopo.auth.dto.response.GoogleLoginResponse;
import com.barcode.recopo.auth.dto.response.LogoutResponse;
import com.barcode.recopo.auth.dto.response.TokenReissueResponse;
import com.barcode.recopo.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 소셜 로그인 (구글)
    @PostMapping("/login/google")
    public ResponseEntity<GoogleLoginResponse> loginWithGoogle(@Valid @RequestBody GoogleLoginRequest request) {
        GoogleLoginResponse response = authService.loginWithGoogle(request.idToken());
        HttpStatus status = response.isNewMember() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    // 토큰 재발급
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenReissueResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.reissueToken(request.refreshToken()));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@AuthenticationPrincipal Long memberId) {
        authService.logout(memberId);
        return ResponseEntity.ok(new LogoutResponse("로그아웃되었습니다."));
    }
}
