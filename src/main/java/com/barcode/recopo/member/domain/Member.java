package com.barcode.recopo.member.domain;

import com.barcode.recopo.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(name = "login_id", length = 50, unique = true)
    private String loginId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 50)
    private String nickname;

    @Column(length = 500)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

    @Column(length = 255, unique = true)
    private String googleSub;

    @Column(nullable = false)
    private boolean cardColorEnabled = false;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    private Member(String email, LoginType loginType, String googleSub, String profileImageUrl) {
        this.email = email;
        this.loginType = loginType;
        this.googleSub = googleSub;
        this.profileImageUrl = profileImageUrl;
    }

    public void updateCardColorEnabled(boolean enabled) {
        this.cardColorEnabled = enabled;
    }

    public void updateProfile(String loginId, String nickname) {
        this.loginId = loginId;
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void resetProfileImage() {
        this.profileImageUrl = null;
    }

    public boolean isProfileCompleted() {
        return nickname != null;
    }

}


