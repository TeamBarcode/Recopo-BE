package com.barcode.recopo.comment.repository;

import com.barcode.recopo.comment.domain.Comment;
import com.barcode.recopo.member.domain.Member;
import com.barcode.recopo.idea.domain.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findAllByIdeaAndDeletedFalseOrderByCreatedAtAsc(Idea idea);
    Optional<Comment> findByCommentIdAndDeletedFalse(Long commentId);
    // 회원 탈퇴 시 본인이 작성한 댓글 삭제
    void deleteAllByMember(Member member);
    // 회원 탈퇴 시 본인 아이디어에 달린 댓글 삭제
    void deleteAllByIdea_Member(Member member);
}
