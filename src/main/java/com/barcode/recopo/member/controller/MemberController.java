package com.barcode.recopo.member.controller;

import com.barcode.recopo.member.dto.request.ProfileSetupRequest;
import com.barcode.recopo.member.dto.response.LoginIdAvailabilityResponse;
import com.barcode.recopo.member.dto.response.MemberProfileResponse;
import com.barcode.recopo.member.dto.response.ProfileImageResponse;
import com.barcode.recopo.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 내 정보 조회
    @GetMapping("/me")
    public MemberProfileResponse getMyProfile(@AuthenticationPrincipal Long memberId) {
        return memberService.getMyProfile(memberId);
    }

    // 아이디 중복 확인
    @GetMapping("/check-login-id")
    public LoginIdAvailabilityResponse checkLoginIdAvailability(@RequestParam String loginId) {
        return memberService.checkLoginIdAvailability(loginId);
    }

    // 프로필 설정 (닉네임 + 아이디)
    @PatchMapping("/me/profile")
    public MemberProfileResponse setupProfile(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody ProfileSetupRequest request
    ) {
        return memberService.setupProfile(memberId, request);
    }

    // 프로필 사진 변경
    @PutMapping(value = "/me/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProfileImageResponse updateProfileImage(
            @AuthenticationPrincipal Long memberId,
            @RequestPart("image") MultipartFile image
    ) {
        return memberService.updateProfileImage(memberId, image);
    }

    // 프로필 사진 삭제 (기본 이미지로 초기화)
    @DeleteMapping("/me/profile-image")
    public ProfileImageResponse resetProfileImage(@AuthenticationPrincipal Long memberId) {
        return memberService.resetProfileImage(memberId);
    }

}
