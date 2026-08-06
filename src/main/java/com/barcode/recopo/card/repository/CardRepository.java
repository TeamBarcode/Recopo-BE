package com.barcode.recopo.card.repository;

import com.barcode.recopo.card.domain.Card;
import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.member.domain.Member;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByMemberMemberIdAndIsConvertedFalse(Long memberId, Sort sort);
    List<Card> findByMemberMemberIdAndCategoryAndIsConvertedFalse(Long memberId, Category category, Sort sort);
    List<Card> findAllByMember_MemberIdAndHashtagContaining(Long memberId, String keyword, Sort sort);
    List<Card> findAllByMember_MemberIdAndCategoryAndHashtagContaining(Long memberId, Category category, String keyword, Sort sort);
    Optional<Card> findByCardIdAndMember_MemberId(Long cardId, Long memberId);

    // 회원이 작성한 카드 개수 조회 (마이페이지용)
    long countByMemberMemberId(Long memberId);

    // 회원 탈퇴 시 본인이 작성한 카드에 연결된 AI 추천 결과를 먼저 지우기 위해 카드 목록 조회
    List<Card> findAllByMember(Member member);

    // 회원 탈퇴 시 본인이 작성한 카드 삭제
    void deleteAllByMember(Member member);
}