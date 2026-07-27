package com.barcode.recopo.idealike.repository;

import com.barcode.recopo.idea.domain.Idea;
import com.barcode.recopo.idealike.domain.IdeaLike;
import com.barcode.recopo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface IdeaLikeRepository extends JpaRepository<IdeaLike, Long>{
    boolean existsByMemberAndIdea(Member member, Idea idea);
    Optional<IdeaLike> findByMemberAndIdea(Member member, Idea idea);
    long countByIdea(Idea idea);
    List<IdeaLike> findAllByMemberOrderByCreatedAtDesc(Member member);
}
