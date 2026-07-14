package com.barcode.recopo.friend.service;

import com.barcode.recopo.friend.domain.FriendRequest;
import com.barcode.recopo.friend.domain.FriendRequestStatus;
import com.barcode.recopo.friend.domain.Friendship;
import com.barcode.recopo.friend.repository.FriendRequestRepository;
import com.barcode.recopo.friend.repository.FriendshipRepository;
import com.barcode.recopo.global.exception.CustomException;
import com.barcode.recopo.global.exception.ErrorCode;
import com.barcode.recopo.member.domain.Member;
import com.barcode.recopo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barcode.recopo.friend.dto.response.FriendRequestResponse;

import java.util.List;
import com.barcode.recopo.friend.dto.response.FriendResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class FriendService {
    private final MemberRepository memberRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;

    @Transactional
    public void sendFriendRequest(Long requesterId, Long receiverId){
        if (requesterId.equals(receiverId)){
            throw new CustomException(ErrorCode.CANNOT_REQUEST_SELF);
        }
        Member requester=findMember(requesterId);
        Member receiver = findMember(receiverId);

        if (isAlreadyFriends(requester, receiver)){
            throw new CustomException(
                    ErrorCode.ALREADY_FRIENDS
            );
        }

        if (hasPendingRequest(requester, receiver)) {
            throw new CustomException(
                    ErrorCode.FRIEND_REQUEST_ALREADY_EXISTS
            );
        }

        FriendRequest friendRequest =
                FriendRequest.create(requester, receiver);
        friendRequestRepository.save(friendRequest);
    }

    public List<FriendRequestResponse> getReceivedRequests(Long receiverId) {
        Member receiver = findMember(receiverId);
        return friendRequestRepository
                .findAllByReceiverAndStatusOrderByCreatedAtDesc(
                        receiver,
                        FriendRequestStatus.PENDING
                )
                .stream()
                .map(request -> new FriendRequestResponse(
                        request.getRequestId(),
                        request.getRequester().getMemberId(),
                        request.getRequester().getNickname(),
                        request.getRequester().getProfileImageUrl(),
                        request.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public void acceptFriendRequest(Long receiverId, Long requestId){
        Member receiver=findMember(receiverId);

        FriendRequest friendRequest=friendRequestRepository
                .findByRequestIdAndReceiver(requestId, receiver)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND)
                );
        if (friendRequest.getStatus() != FriendRequestStatus.PENDING){
            throw new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
        }

        if (isAlreadyFriends(
                friendRequest.getRequester(),
                friendRequest.getReceiver()
        )){
            throw new CustomException(ErrorCode.ALREADY_FRIENDS);
        }

        friendRequest.accept();

        Friendship friendship = Friendship.create(
                friendRequest.getRequester(),
                friendRequest.getReceiver()
        );

        friendshipRepository.save(friendship);
    }

    @Transactional
    public void rejectFriendRequest(Long receiverId, Long requestId) {
        Member receiver = findMember(receiverId);

        FriendRequest friendRequest = friendRequestRepository
                .findByRequestIdAndReceiver(requestId, receiver)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND)
                );

        if (friendRequest.getStatus() != FriendRequestStatus.PENDING) {
            throw new CustomException(
                    ErrorCode.FRIEND_REQUEST_NOT_FOUND
            );
        }

        friendRequest.reject();
    }

    public List<FriendResponse> getFriends(Long memberId) {
        Member loginMember = findMember(memberId);

        return friendshipRepository
                .findAllByMemberOrFriendOrderByCreatedAtDesc(
                        loginMember,
                        loginMember
                )
                .stream()
                .map(friendship -> {
                    Member friend;

                    if (friendship.getMember()
                            .getMemberId()
                            .equals(memberId)) {
                        friend = friendship.getFriend();
                    } else {
                        friend = friendship.getMember();
                    }

                    return new FriendResponse(
                            friend.getMemberId(),
                            friend.getNickname(),
                            friend.getProfileImageUrl()
                    );
                })
                .toList();
    }

    private Member findMember(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException(ErrorCode.BAD_REQUEST)
        );
    }

    private boolean isAlreadyFriends(Member first, Member second) {
        return friendshipRepository.existsByMemberAndFriend(first, second)
                || friendshipRepository.existsByMemberAndFriend(second, first);
    }

    private boolean hasPendingRequest(Member first, Member second) {
        return friendRequestRepository.existsByRequesterAndReceiverAndStatus(
                first,
                second,
                FriendRequestStatus.PENDING
        ) || friendRequestRepository.existsByRequesterAndReceiverAndStatus(
                second,
                first,
                FriendRequestStatus.PENDING
        );
    }
}