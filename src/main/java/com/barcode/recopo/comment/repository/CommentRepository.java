package com.barcode.recopo.comment.repository;

import com.barcode.recopo.comment.domain.Comment;
import com.barcode.recopo.idea.domain.Idea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findAllByIdeaAndDeletedFalseOrderByCreatedAtAsc(Idea idea);
    Optional<Comment> findByCommentIdAndDeletedFalse(Long commentId);
}
