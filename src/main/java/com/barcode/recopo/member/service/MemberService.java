package com.barcode.recopo.member.service;

import com.barcode.recopo.auth.service.AuthService;
import com.barcode.recopo.card.repository.CardRepository;
import com.barcode.recopo.comment.repository.CommentRepository;
import com.barcode.recopo.friend.repository.FriendRequestRepository;
import com.barcode.recopo.friend.repository.FriendshipRepository;
import com.barcode.recopo.global.exception.CustomException;
import com.barcode.recopo.global.exception.ErrorCode;
import com.barcode.recopo.idea.repository.IdeaRepository;
import com.barcode.recopo.idealike.repository.IdeaLikeRepository;
import com.barcode.recopo.member.domain.Member;
import com.barcode.recopo.member.dto.request.CardColorSettingRequest;
import com.barcode.recopo.member.dto.request.ProfileSetupRequest;
import com.barcode.recopo.member.dto.request.ProfileUpdateRequest;
import com.barcode.recopo.member.dto.response.CardColorSettingResponse;
import com.barcode.recopo.member.dto.response.LoginIdAvailabilityResponse;
import com.barcode.recopo.member.dto.response.MemberProfileResponse;
import com.barcode.recopo.member.dto.response.MemberWithdrawalResponse;
import com.barcode.recopo.member.dto.response.ProfileImageResponse;
import com.barcode.recopo.member.dto.response.ProfileUpdateResponse;
import com.barcode.recopo.member.repository.MemberRepository;
import com.barcode.recopo.member.storage.ImageStorage;
import com.barcode.recopo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    private final MemberRepository memberRepository;
    private final ImageStorage imageStorage;
    private final CardRepository cardRepository;
    private final IdeaRepository ideaRepository;
    private final CommentRepository commentRepository;
    private final IdeaLikeRepository ideaLikeRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final NotificationRepository notificationRepository;
    private final AuthService authService;

    // 내 정보 조회
    public MemberProfileResponse getMyProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        return MemberProfileResponse.from(member, cardRepository.countByMemberMemberId(memberId), ideaRepository.countByMember_MemberId(memberId));
    }

    // 아이디 중복 확인
    public LoginIdAvailabilityResponse checkLoginIdAvailability(String loginId) {
        boolean available = !memberRepository.existsByLoginId(loginId);
        return new LoginIdAvailabilityResponse(loginId, available);
    }

    // 프로필 설정 (닉네임 + 아이디)
    @Transactional
    public MemberProfileResponse setupProfile(Long memberId, ProfileSetupRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!request.loginId().equals(member.getLoginId()) && memberRepository.existsByLoginId(request.loginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

        member.updateProfile(request.loginId(), request.nickname());
        return MemberProfileResponse.from(member, cardRepository.countByMemberMemberId(memberId), ideaRepository.countByMember_MemberId(memberId));
    }

    // 프로필 부분 수정 (아이디/닉네임 중 하나 또는 둘 다)
    @Transactional
    public ProfileUpdateResponse updateProfile(Long memberId, ProfileUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean hasLoginId = StringUtils.hasText(request.loginId());
        boolean hasNickname = StringUtils.hasText(request.nickname());

        if (!hasLoginId && !hasNickname) {
            throw new CustomException(ErrorCode.EMPTY_PROFILE_UPDATE_REQUEST);
        }

        if (hasLoginId && !request.loginId().equals(member.getLoginId()) && memberRepository.existsByLoginId(request.loginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

        member.updateProfile(
                hasLoginId ? request.loginId() : member.getLoginId(),
                hasNickname ? request.nickname() : member.getNickname()
        );

        return ProfileUpdateResponse.from(member);
    }

    // 브레인스토밍 카드 색상 반영 설정 변경 (ON/OFF)
    @Transactional
    public CardColorSettingResponse updateCardColorEnabled(Long memberId, CardColorSettingRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateCardColorEnabled(request.cardColorEnabled());
        return new CardColorSettingResponse(member.isCardColorEnabled());
    }

    // 프로필 사진 변경
    @Transactional
    public ProfileImageResponse updateProfileImage(Long memberId, MultipartFile image) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        validateImage(image);

        String key = memberId + "_" + System.currentTimeMillis() + extractExtension(image.getOriginalFilename());
        member.updateProfileImage(imageStorage.upload(image, key));
        memberRepository.flush();

        return ProfileImageResponse.from(member);
    }

    // 프로필 사진 삭제 (기본 이미지로 초기화)
    @Transactional
    public ProfileImageResponse resetProfileImage(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.resetProfileImage();
        memberRepository.flush();

        return ProfileImageResponse.from(member);
    }

    // 회원 탈퇴 (Hard Delete) - FK 참조 관계 역순으로 자식 데이터부터 삭제
    @Transactional
    public MemberWithdrawalResponse withdraw(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        commentRepository.deleteAllByMember(member);
        commentRepository.deleteAllByIdea_Member(member);
        ideaLikeRepository.deleteAllByMember(member);
        ideaLikeRepository.deleteAllByIdea_Member(member);
        notificationRepository.deleteAllByReceiver(member);
        notificationRepository.deleteAllByActor(member);
        ideaRepository.deleteAllByMember(member);
        cardRepository.deleteAllByMember(member);
        friendRequestRepository.deleteAllByRequester(member);
        friendRequestRepository.deleteAllByReceiver(member);
        friendshipRepository.deleteAllByMember(member);
        friendshipRepository.deleteAllByFriend(member);

        String profileImageUrl = member.getProfileImageUrl();
        memberRepository.delete(member);

        // DB 트랜잭션이 실제로 commit된 후에만 되돌릴 수 없는 외부 자원(파일, Redis)을 정리한다
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (profileImageUrl != null) {
                    imageStorage.delete(profileImageUrl);
                }
                authService.logout(memberId);
            }
        });

        return new MemberWithdrawalResponse("회원 탈퇴가 완료되었습니다.");
    }

    // 다른 도메인에서 회원 조회 시 사용하는 helper method
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void validateImage(MultipartFile image) {
        if (image == null || image.isEmpty() || !ALLOWED_IMAGE_TYPES.contains(image.getContentType())) {
            throw new CustomException(ErrorCode.INVALID_IMAGE_TYPE);
        }
        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new CustomException(ErrorCode.IMAGE_TOO_LARGE);
        }
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }

}
