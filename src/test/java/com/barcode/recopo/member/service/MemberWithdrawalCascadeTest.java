package com.barcode.recopo.member.service;

import com.barcode.recopo.card.domain.Card;
import com.barcode.recopo.card.domain.Category;
import com.barcode.recopo.card.repository.CardRepository;
import com.barcode.recopo.comment.domain.Comment;
import com.barcode.recopo.comment.repository.CommentRepository;
import com.barcode.recopo.friend.domain.FriendRequest;
import com.barcode.recopo.friend.domain.Friendship;
import com.barcode.recopo.friend.repository.FriendRequestRepository;
import com.barcode.recopo.friend.repository.FriendshipRepository;
import com.barcode.recopo.idea.domain.Idea;
import com.barcode.recopo.idea.domain.Visibility;
import com.barcode.recopo.idea.repository.IdeaRepository;
import com.barcode.recopo.idealike.domain.IdeaLike;
import com.barcode.recopo.idealike.repository.IdeaLikeRepository;
import com.barcode.recopo.member.domain.LoginType;
import com.barcode.recopo.member.domain.Member;
import com.barcode.recopo.member.repository.MemberRepository;
import com.barcode.recopo.notification.domain.Notification;
import com.barcode.recopo.notification.domain.NotificationTargetType;
import com.barcode.recopo.notification.domain.NotificationType;
import com.barcode.recopo.notification.repository.NotificationRepository;
import com.barcode.recopo.recommendation.domain.Recommendation;
import com.barcode.recopo.recommendation.repository.RecommendationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 회원 탈퇴(Hard Delete) 시 관련 도메인 데이터가 실제 MySQL에서
 * 빠짐없이 cascade 삭제되는지 검증하는 통합 테스트.
 * 전체를 하나의 트랜잭션으로 묶어 detached entity 문제를 피하고,
 * 테스트 종료 후 자동 롤백되어 DB에 흔적을 남기지 않는다.
 */
@SpringBootTest
@Transactional
class
MemberWithdrawalCascadeTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired CardRepository cardRepository;
    @Autowired IdeaRepository ideaRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired IdeaLikeRepository ideaLikeRepository;
    @Autowired FriendRequestRepository friendRequestRepository;
    @Autowired FriendshipRepository friendshipRepository;
    @Autowired NotificationRepository notificationRepository;
    @Autowired RecommendationRepository recommendationRepository;

    @Test
    void withdraw_deletes_all_related_data_across_every_repository() {
        // ---------- given ----------
        Member target = memberRepository.save(Member.builder()
                .email("withdraw-target-" + System.nanoTime() + "@test.com")
                .loginType(LoginType.GOOGLE)
                .googleSub("sub-target-" + System.nanoTime())
                .build());

        Member other = memberRepository.save(Member.builder()
                .email("withdraw-other-" + System.nanoTime() + "@test.com")
                .loginType(LoginType.GOOGLE)
                .googleSub("sub-other-" + System.nanoTime())
                .build());

        // target's own card -> idea (+ AI recommendation attached)
        Card targetCard = cardRepository.save(Card.create("t-title", "t-content", Category.ETC, "#tag", target));
        Recommendation recommendation = recommendationRepository.save(Recommendation.create(
                targetCard.getCardId(), 111L, "repo-name", "owner/repo-name",
                "https://github.com/owner/repo-name", "desc", "Java", "Spring, JPA",
                10, 2, "2026-01-01", "reason"
        ));
        Idea targetIdea = ideaRepository.save(Idea.createWithRecommendation(targetCard, Visibility.PUBLIC, recommendation));

        // other's own card -> idea, so we can test target liking/commenting on someone else's idea
        Card otherCard = cardRepository.save(Card.create("o-title", "o-content", Category.ETC, "#tag", other));
        Idea otherIdea = ideaRepository.save(Idea.create(otherCard, Visibility.PUBLIC));

        // comments: target comments on own idea, other comments on target's idea
        commentRepository.save(Comment.create("target's own comment", target, targetIdea));
        commentRepository.save(Comment.create("other's comment on target idea", other, targetIdea));

        // idea likes: target likes other's idea, other likes target's idea
        ideaLikeRepository.save(IdeaLike.create(target, otherIdea));
        ideaLikeRepository.save(IdeaLike.create(other, targetIdea));

        // friend request + friendship both directions
        friendRequestRepository.save(FriendRequest.create(target, other));
        friendshipRepository.save(Friendship.create(target, other));

        // notifications: target as receiver, target as actor
        notificationRepository.save(Notification.create(NotificationType.FRIEND_REQUEST, "알림1", target.getMemberId(),
                NotificationTargetType.FRIEND_REQUEST, other, target));
        notificationRepository.save(Notification.create(NotificationType.LIKE, "알림2", otherIdea.getIdeaId(),
                NotificationTargetType.IDEA, target, other));

        Long targetId = target.getMemberId();
        Long otherId = other.getMemberId();
        Long targetCardId = targetCard.getCardId();

        // ---------- when ----------
        memberService.withdraw(targetId);

        // ---------- then ----------
        assertThat(memberRepository.findById(targetId)).isEmpty();
        assertThat(memberRepository.findById(otherId)).isPresent(); // 다른 회원은 영향 없어야 함

        assertThat(cardRepository.countByMemberMemberId(targetId)).isZero();
        assertThat(ideaRepository.findAllByMember_MemberId(targetId, Sort.unsorted())).isEmpty();
        assertThat(commentRepository.findByCommentIdAndDeletedFalse(targetIdea.getIdeaId())).isEmpty();
        assertThat(ideaLikeRepository.findAllByMemberOrderByCreatedAtDesc(target)).isEmpty();
        assertThat(friendRequestRepository.existsByRequesterAndReceiverAndStatus(
                target, other, com.barcode.recopo.friend.domain.FriendRequestStatus.PENDING)).isFalse();
        assertThat(friendshipRepository.findAllByMemberOrFriendOrderByCreatedAtDesc(target, target)).isEmpty();

        // other 소유 데이터는 남아있어야 함 (target의 좋아요만 지워짐)
        assertThat(ideaRepository.findById(otherIdea.getIdeaId())).isPresent();
        assertThat(ideaLikeRepository.existsByMemberAndIdea(other, targetIdea)).isFalse(); // targetIdea 자체가 삭제됨

        // 탈퇴 회원의 카드에 달린 AI 추천 결과도 함께 삭제되어야 한다 (고아 row 방지)
        List<Recommendation> leftover = recommendationRepository.findAllByCardId(targetCardId, Sort.unsorted());
        assertThat(leftover).as("Recommendation은 삭제된 card_id를 참조하는 고아 row를 남기면 안 된다").isEmpty();
    }
}
