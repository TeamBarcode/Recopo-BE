package com.barcode.recopo.idea.repository;

import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.idea.domain.Idea;
import com.barcode.recopo.idea.domain.Visibility;
import org.springframework.data.domain.Sort;
import com.barcode.recopo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {
    List<Idea> findAllByMember_MemberId(Long memberId, Sort sort);
    List<Idea> findAllByMember_MemberIdAndCategory(Long memberId, Category category, Sort sort);
    List<Idea> findAllByMember_MemberIdAndHashtagContaining(Long memberId, String keyword, Sort sort);
    List<Idea> findAllByMember_MemberIdAndCategoryAndHashtagContaining(Long memberId, Category category, String keyword, Sort sort);

    List<Idea> findAllByMember_MemberIdAndVisibility(Long memberId, Visibility visibility, Sort sort);
    List<Idea> findAllByMember_MemberIdAndCategoryAndVisibility(Long memberId, Category category, Visibility visibility, Sort sort);
    List<Idea> findAllByMember_MemberIdAndHashtagContainingAndVisibility(Long memberId, String keyword, Visibility visibility, Sort sort);
    List<Idea> findAllByMember_MemberIdAndCategoryAndHashtagContainingAndVisibility(Long memberId, Category category, String keyword, Visibility visibility, Sort sort);

    Optional<Idea> findByIdeaIdAndMember_MemberId(Long ideaId, Long memberId);

    // 회원이 작성한 아이디어 개수 조회 (마이페이지용)
    long countByMember_MemberId(Long memberId);

    // 회원 탈퇴 시 본인이 작성한 아이디어 삭제
    void deleteAllByMember(Member member);
}
