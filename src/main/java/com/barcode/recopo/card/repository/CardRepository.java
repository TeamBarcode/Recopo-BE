package com.barcode.recopo.card.repository;

import com.barcode.recopo.card.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    // 사용자가 입력한 해시태그 단어가 포함된 카드들을 조회하는 메서드
    List<Card> findByHashtagContaining(String hashtag);
}