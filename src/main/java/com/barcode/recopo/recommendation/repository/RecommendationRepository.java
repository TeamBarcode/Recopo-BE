package com.barcode.recopo.recommendation.repository;

import com.barcode.recopo.recommendation.domain.Recommendation;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findAllByCardId(Long cardId, Sort sort);

    // 회원 탈퇴 시 본인이 작성한 카드에 달린 AI 추천 결과 삭제 (cardId는 연관관계가 아닌 단순 컬럼이라 별도 삭제 필요)
    void deleteAllByCardIdIn(List<Long> cardIds);

}