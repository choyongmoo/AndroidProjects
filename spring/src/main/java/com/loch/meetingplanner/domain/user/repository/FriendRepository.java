package com.loch.meetingplanner.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loch.meetingplanner.domain.user.model.Friend;
import com.loch.meetingplanner.domain.user.model.FriendStatus;
import com.loch.meetingplanner.domain.user.model.User;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    // 두 유저 간의 친구 관계 조회 (단방향)
    Optional<Friend> findByUserAndFriend(User user, User friend);

    // 친구 상태까지 포함
    Optional<Friend> findByUserAndFriendAndStatus(User user, User friend, FriendStatus status);

    // 친구 요청 목록 (내가 보낸 요청)
    List<Friend> findByUserAndStatus(User user, FriendStatus status);

    // 받은 요청 목록
    List<Friend> findByFriendAndStatus(User user, FriendStatus status);

    // 친구 목록 (status = ACCEPTED)
    List<Friend> findByUserAndStatusOrFriendAndStatus(User user, FriendStatus status1, User friend,
            FriendStatus status2);

    // 친구 관계 존재 여부 (양방향 체크는 서비스에서 2번 호출)
    boolean existsByUserAndFriendAndStatus(User user, User friend, FriendStatus status);
    
}
