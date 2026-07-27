package com.barcode.recopo.comment.repository;

import com.barcode.recopo.comment.domain.Comment;
import com.barcode.recopo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 회원 탈퇴 시 본인이 작성한 댓글 삭제
    void deleteAllByMember(Member member);

    // 회원 탈퇴 시 본인 아이디어에 달린 댓글 삭제
    void deleteAllByIdea_Member(Member member);
}
