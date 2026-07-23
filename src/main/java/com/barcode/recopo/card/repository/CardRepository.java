package com.barcode.recopo.card.repository;

import com.barcode.recopo.card.domain.Card;
import com.barcode.recopo.card.domain.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByMemberMemberId(Long memberId);
    List<Card> findByMemberMemberId(Long memberId, Sort sort);
    List<Card> findByMemberMemberIdAndCategory(Long member_memberId, Category category, Sort sort);
    List<Card> findByMemberMemberIdAndIsConvertedFalse(Long memberId, Sort sort);
    List<Card> findByMemberMemberIdAndCategoryAndIsConvertedFalse(Long memberId, Category category, Sort sort);
    List<Card> findByHashtagContaining(String hashtag);

    Optional<Card> findByCardIdAndMember_MemberId(Long cardId, Long memberId);

    // 회원이 작성한 카드 개수 조회 (마이페이지용)
    long countByMemberMemberId(Long memberId);
}