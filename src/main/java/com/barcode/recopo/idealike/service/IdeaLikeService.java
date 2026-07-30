package com.barcode.recopo.idealike.service;

import com.barcode.recopo.global.exception.CustomException;
import com.barcode.recopo.global.exception.ErrorCode;
import com.barcode.recopo.idea.dto.IdeaResponseDto;
import com.barcode.recopo.idea.domain.Idea;
import com.barcode.recopo.idea.repository.IdeaRepository;
import com.barcode.recopo.idealike.domain.IdeaLike;
import com.barcode.recopo.idealike.dto.response.IdeaLikeResponse;
import com.barcode.recopo.idealike.repository.IdeaLikeRepository;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IdeaLikeService {

    private final IdeaLikeRepository ideaLikeRepository;
    private final IdeaRepository ideaRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public IdeaLikeResponse likeIdea(Long memberId, long ideaId){
        Member member=memberRepository.findById(memberId).orElseThrow(()->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Idea idea=ideaRepository.findById(ideaId).orElseThrow(()->
                new CustomException(ErrorCode.IDEA_NOT_FOUND));
        if (idea.getMember().getMemberId().equals(memberId)){
            throw new CustomException(ErrorCode.CANNOT_LIKE_OWN_IDEA);
        }
        if (ideaLikeRepository.existsByMemberAndIdea(member, idea)){
            throw new CustomException(ErrorCode.IDEA_LIKE_ALREADY_EXISTS);
        }

        IdeaLike ideaLike=IdeaLike.create(member, idea);
        ideaLikeRepository.save(ideaLike);

        Notification notification=Notification.create(
                NotificationType.LIKE,
                member.getNickname()+"님이 회원님의 아이디어를 좋아합니다",
                idea.getIdeaId(),
                NotificationTargetType.IDEA,
                idea.getMember(),
                member
        );
        notificationRepository.save(notification);

        long likeCount=ideaLikeRepository.countByIdea(idea);

        return new IdeaLikeResponse(
                idea.getIdeaId(),
                true,
                likeCount
        );
    }

    @Transactional
    public IdeaLikeResponse unlikeIdea(Long memberId, Long ideaId){
        Member member=memberRepository.findById(memberId).orElseThrow(()->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Idea idea=ideaRepository.findById(ideaId).orElseThrow(()->
                new CustomException(ErrorCode.IDEA_NOT_FOUND));

        IdeaLike ideaLike=ideaLikeRepository.findByMemberAndIdea(member, idea).orElseThrow(()->
                new CustomException(ErrorCode.IDEA_LIKE_NOT_FOUND));

        ideaLikeRepository.delete(ideaLike);

        notificationRepository.deleteByReceiverAndActorAndTypeAndTargetTypeAndTargetId(
                idea.getMember(),
                member,
                NotificationType.LIKE,
                NotificationTargetType.IDEA,
                idea.getIdeaId()
        );

        long likeCount=ideaLikeRepository.countByIdea(idea);

        return new IdeaLikeResponse(
                idea.getIdeaId(),
                false,
                likeCount
        );
    }

    public IdeaLikeResponse getIdeaLikeStatus(Long memberId, Long ideaId) {
        Member member = memberRepository.findById(memberId).orElseThrow(()->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Idea idea=ideaRepository.findById(ideaId).orElseThrow(()->
                new CustomException(ErrorCode.IDEA_NOT_FOUND));

        boolean liked=ideaLikeRepository.existsByMemberAndIdea(member, idea);
        long likeCount=ideaLikeRepository.countByIdea(idea);

        return new IdeaLikeResponse(
                idea.getIdeaId(), liked, likeCount
        );
    }

    public List<IdeaResponseDto> getLikedIdeas(Long memberId){
        Member member=memberRepository.findById(memberId).orElseThrow(()->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return ideaLikeRepository.findAllByMemberOrderByCreatedAtDesc(member)
                .stream()
                .map(IdeaLike::getIdea)
                .map(IdeaResponseDto::from)
                .toList();
    }
}