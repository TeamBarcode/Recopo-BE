package com.barcode.recopo.comment.service;

import com.barcode.recopo.comment.domain.Comment;
import com.barcode.recopo.comment.dto.request.CommentCreateRequest;
import com.barcode.recopo.comment.dto.request.CommentUpdateRequest;
import com.barcode.recopo.comment.dto.response.CommentResponse;
import com.barcode.recopo.comment.repository.CommentRepository;

import com.barcode.recopo.global.exception.CustomException;
import com.barcode.recopo.global.exception.ErrorCode;
import com.barcode.recopo.idea.domain.Idea;
import com.barcode.recopo.idea.repository.IdeaRepository;
import com.barcode.recopo.member.domain.Member;
import com.barcode.recopo.member.repository.MemberRepository;
import com.barcode.recopo.notification.domain.Notification;
import com.barcode.recopo.notification.domain.NotificationTargetType;
import com.barcode.recopo.notification.domain.NotificationType;
import com.barcode.recopo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final IdeaRepository ideaRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public CommentResponse createComment(
            Long memberId,
            Long ideaId,
            CommentCreateRequest request
    ){
        Member member=memberRepository.findById(memberId).orElseThrow(()->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Idea idea=ideaRepository.findById(ideaId).orElseThrow(()->
                new CustomException(ErrorCode.IDEA_NOT_FOUND));

        Comment parent = null;
        Comment comment;
        if (request.parentCommentId() == null) {
            comment = Comment.create(request.content(), member, idea);
        } else {
            parent = commentRepository
                    .findByCommentIdAndDeletedFalse(request.parentCommentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

            if (!parent.getIdea().getIdeaId().equals(ideaId)) {
                throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
            }

            if (parent.getParentCommentId() != null) {
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }

            comment = Comment.createReply(request.content(), member, parent);
        }
        commentRepository.save(comment);

        Member receiver = parent == null ? idea.getMember() : parent.getMember();

        if (!receiver.getMemberId().equals(memberId)) {
            NotificationType type = parent == null
                    ? NotificationType.COMMENT
                    : NotificationType.REPLY;
            String message = parent == null
                    ? member.getNickname() + "님이 회원님의 아이디어에 댓글을 남겼습니다."
                    : member.getNickname() + "님이 회원님의 댓글에 답글을 남겼습니다.";

            Notification notification = Notification.create(
                    type,
                    message,
                    idea.getIdeaId(),
                    NotificationTargetType.IDEA,
                    receiver,
                    member
            );
            notification.setCommentId(comment.getCommentId());
            notificationRepository.save(notification);
        }
        return new CommentResponse(
                comment.getCommentId(),
                member.getMemberId(),
                member.getNickname(),
                member.getProfileImageUrl(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getParentCommentId()
        );
    }
    public List<CommentResponse> getComments(Long ideaId){
        Idea idea=ideaRepository.findById(ideaId).orElseThrow(()->
                new CustomException(ErrorCode.IDEA_NOT_FOUND));

        List<Comment> comments = commentRepository
                .findAllByIdeaAndDeletedFalseOrderByCreatedAtAsc(idea);

        Set<Long> visibleParentIds = comments.stream()
                .filter(comment -> comment.getParentCommentId() == null)
                .map(Comment::getCommentId)
                .collect(Collectors.toSet());

        return comments.stream()
                .filter(comment -> comment.getParentCommentId() == null
                        || visibleParentIds.contains(comment.getParentCommentId()))
                .map(comment -> new CommentResponse(
                        comment.getCommentId(),
                        comment.getMember().getMemberId(),
                        comment.getMember().getNickname(),
                        comment.getMember().getProfileImageUrl(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt(),
                        comment.getParentCommentId()
                ))
                .toList();
    }

    @Transactional
    public CommentResponse updateComment(
            Long memberId,
            Long commentId,
            CommentUpdateRequest request
    ){
        Comment comment=commentRepository.findByCommentIdAndDeletedFalse(commentId).orElseThrow(()->
                new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.getMember().getMemberId().equals(memberId)){
            throw new CustomException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS
            );
        }
        comment.update(request.content());

        return new CommentResponse(
                comment.getCommentId(),
                comment.getMember().getMemberId(),
                comment.getMember().getNickname(),
                comment.getMember().getProfileImageUrl(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getParentCommentId()
        );
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId){
        Comment comment=commentRepository.findByCommentIdAndDeletedFalse(commentId).orElseThrow(()->
                new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMember().getMemberId().equals(memberId)){
            throw new CustomException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
        }
        comment.delete();
    }
}