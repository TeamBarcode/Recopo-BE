package com.barcode.recopo.idea.repository;

import com.barcode.recopo.idea.domain.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {
    List<Idea> findAllByMember_MemberId(Long memberId);
    Optional<Idea> findByIdeaIdAndMember_MemberId(Long ideaId, Long memberId);
}
