package com.barcode.recopo.idealike.repository;

import com.barcode.recopo.idealike.domain.IdeaLike;
import com.barcode.recopo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdeaLikeRepository extends JpaRepository<IdeaLike, Long> {

    // 회원 탈퇴 시 본인이 누른 좋아요 삭제
    void deleteAllByMember(Member member);

    // 회원 탈퇴 시 본인 아이디어에 달린 좋아요 삭제
    void deleteAllByIdea_Member(Member member);
}
