package com.barcode.recopo.card.repository;

import com.barcode.recopo.card.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByMemberMemberId(Long memberId);

    List<Card> findByHashtagContaining(String hashtag);
}