package com.barcode.recopo.friend.repository;

import com.barcode.recopo.friend.domain.FriendRequest;
import com.barcode.recopo.friend.domain.FriendRequestStatus;
import com.barcode.recopo.member.domain.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository
        extends JpaRepository<FriendRequest, Long>{

    boolean existsByRequesterAndReceiverAndStatus(
            Member requester,
            Member receiver,
            FriendRequestStatus status
    );

    List<FriendRequest> findAllByReceiverAndStatusOrderByCreatedAtDesc(
            Member receiver,
            FriendRequestStatus status
    );

    List<FriendRequest> findAllByRequesterAndStatusOrderByCreatedAtDesc(
            Member requester,
            FriendRequestStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<FriendRequest> findByRequestIdAndReceiver(Long requestId, Member receiver);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<FriendRequest> findByRequestIdAndRequester(Long requestId, Member requester);

    // 회원 탈퇴 시 본인이 보낸/받은 친구 요청 삭제
    void deleteAllByRequester(Member requester);
    void deleteAllByReceiver(Member receiver);
}
