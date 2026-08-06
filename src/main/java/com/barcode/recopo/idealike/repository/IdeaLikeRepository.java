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
    // 회원 탈퇴 시 본인이 누른 좋아요 삭제
    void deleteAllByMember(Member member);

    // 회원 탈퇴 시 본인 아이디어에 달린 좋아요 삭제
    void deleteAllByIdea_Member(Member member);
}
