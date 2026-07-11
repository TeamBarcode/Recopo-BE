package com.barcode.recopo.friend.repository;

import com.barcode.recopo.friend.domain.FriendRequest;
import com.barcode.recopo.friend.domain.FriendRequestStatus;
import com.barcode.recopo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

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

    Optional<FriendRequest> findByRequestIdAndReceiver(
            Long requestId,
            Member receiver
    );
}
