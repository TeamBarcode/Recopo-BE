package com.barcode.recopo.recommendation.repository;

import com.barcode.recopo.recommendation.domain.Recommendation;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findAllByCardId(Long cardId, Sort sort);

}