package com.barcode.recopo.friend.repository;

import com.barcode.recopo.friend.domain.Friendship;
import com.barcode.recopo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FrienshipRepository
        extends JpaRepository<Friendship, Long>{

    boolean existsByMemberAndFriend(
            Member member,
            Member friend
    );

    List<Friendship> findAllByMemberOrFriendOrderByCreatedAtDesc(
            Member member,
            Member friend
    );

    Optional<Friendship> findByMemberAndFriend(
            Member member,
            Member friend
    );
}
