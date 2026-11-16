package com.barcode.recopo.member.repository;

import com.barcode.recopo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByGoogleSub(String googleSub);

    boolean existsByLoginId(String loginId);

    List<Member> findTop20ByNicknameContainingIgnoreCaseAndDeletedFalseAndMemberIdNotOrderByNicknameAscMemberIdAsc(
            String keyword,
            Long currentMemberId
    );

}
