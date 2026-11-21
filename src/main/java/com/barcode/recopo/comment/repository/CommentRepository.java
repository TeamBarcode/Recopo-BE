package com.barcode.recopo.comment.repository;

import com.barcode.recopo.comment.domain.Comment;
import com.barcode.recopo.member.domain.Member;
import com.barcode.recopo.idea.domain.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findAllByIdeaAndDeletedFalseOrderByCreatedAtAsc(Idea idea);
    Optional<Comment> findByCommentIdAndDeletedFalse(Long commentId);
    long countByIdeaAndDeletedFalse(Idea idea);

    // 회원 탈퇴 시 본인이 작성한 댓글 삭제
    void deleteAllByMember(Member member);
    // 회원 탈퇴 시 본인 아이디어에 달린 댓글 삭제
    void deleteAllByIdea_Member(Member member);

    @Query("""
        select count(c)
        from Comment c
        where c.idea = :idea
          and c.deleted = false
          and (
              c.parentCommentId is null
              or exists (
                  select p.commentId
                  from Comment p
                  where p.commentId = c.parentCommentId
                    and p.idea = c.idea
                    and p.parentCommentId is null
                    and p.deleted = false
              )
          )
        """)
    long countVisibleComments(@Param("idea") Idea idea);
}