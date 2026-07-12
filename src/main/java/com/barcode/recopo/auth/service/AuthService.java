package com.barcode.recopo.auth.service;

import com.barcode.recopo.auth.client.GoogleTokenVerifier;
import com.barcode.recopo.auth.client.GoogleUserInfo;
import com.barcode.recopo.auth.dto.response.GoogleLoginResponse;
import com.barcode.recopo.auth.dto.response.TokenReissueResponse;
import com.barcode.recopo.auth.repository.RefreshTokenRepository;
import com.barcode.recopo.global.exception.CustomException;
import com.barcode.recopo.global.exception.ErrorCode;
import com.barcode.recopo.global.jwt.JwtProvider;
import com.barcode.recopo.member.domain.LoginType;
import com.barcode.recopo.member.domain.Member;
import com.barcode.recopo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 소셜 로그인 (구글)
    @Transactional
    public GoogleLoginResponse loginWithGoogle(String idToken) {
        GoogleUserInfo googleUserInfo = googleTokenVerifier.verify(idToken);

        Member member = memberRepository.findByGoogleSub(googleUserInfo.sub()).orElse(null);
        boolean isNewMember = member == null;

        if (isNewMember) {
            member = memberRepository.save(Member.builder()
                    .email(googleUserInfo.email())
                    .loginType(LoginType.GOOGLE)
                    .googleSub(googleUserInfo.sub())
                    .profileImageUrl(googleUserInfo.picture())
                    .build());
        }

        String accessToken = jwtProvider.generateAccessToken(member.getMemberId());
        String refreshToken = jwtProvider.generateRefreshToken(member.getMemberId());
        saveRefreshToken(member.getMemberId(), refreshToken);

        return GoogleLoginResponse.of(member, accessToken, refreshToken, isNewMember);
    }

    // 토큰 재발급
    public TokenReissueResponse reissueToken(String refreshToken) {
        if (!jwtProvider.isValid(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long memberId = jwtProvider.getMemberId(refreshToken);
        String savedRefreshToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (!savedRefreshToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = jwtProvider.generateAccessToken(memberId);
        return new TokenReissueResponse(newAccessToken);
    }

    // 로그아웃
    public void logout(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }

    private void saveRefreshToken(Long memberId, String refreshToken) {
        refreshTokenRepository.save(memberId, refreshToken, Duration.ofMillis(jwtProvider.getRefreshTokenValidity()));
    }
}
