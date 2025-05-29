package com.loch.meetingplanner.domain.user.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.loch.meetingplanner.domain.user.dto.FriendAcceptDto;
import com.loch.meetingplanner.domain.user.dto.FriendRequest;
import com.loch.meetingplanner.domain.user.dto.GetUserResponse;
import com.loch.meetingplanner.domain.user.model.Friend;
import com.loch.meetingplanner.domain.user.model.FriendStatus;
import com.loch.meetingplanner.domain.user.model.User;
import com.loch.meetingplanner.domain.user.repository.FriendRepository;
import com.loch.meetingplanner.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public void sendRequest(String requesterUsername, FriendRequest request) {
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("요청자 없음"));

        User target = userRepository.findByUsername(request.targetUsername())
                .orElseThrow(() -> new RuntimeException("대상 유저 없음"));

        if (friendRepository.findByUserAndFriend(requester, target).isPresent()) {
            throw new RuntimeException("이미 친구 상태입니다");
        }

        Friend friend = Friend.builder()
                .user(requester)
                .friend(target)
                .status(FriendStatus.PENDING)
                .build();
        friendRepository.save(friend);
    }

    public void acceptFriendRequest(User accepter, FriendAcceptDto dto) {
        User requester = userRepository.findByUsername(dto.requesterUsername())
                .orElseThrow(() -> new RuntimeException("요청자 없음"));

        Friend request = friendRepository.findByUserAndFriend(requester, accepter)
                .orElseThrow(() -> new RuntimeException("요청 없음"));

        request.setStatus(FriendStatus.ACCEPTED);

        Friend reverse = Friend.builder()
                .user(accepter)
                .friend(requester)
                .status(FriendStatus.ACCEPTED)
                .build();

        friendRepository.save(reverse);
    }

    public void rejectFriendRequest(User user, String requesterUsername) {
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("요청자 없음"));

        Friend request = friendRepository.findByUserAndFriend(requester, user)
                .orElseThrow(() -> new RuntimeException("요청 없음"));

        friendRepository.delete(request);
    }

    // 친구 요청 받은거나 보낸것중 수락된 친구들을 보여줌
    public List<GetUserResponse> getFriends(User user) {
        return friendRepository.findByUserAndStatus(user, FriendStatus.ACCEPTED)
                .stream()
                .map(f -> {
                    User friend = f.getFriend();
                    return new GetUserResponse(friend.getUsername(), friend.getDisplayName(), friend.getEmail(), friend.getProfileImageUrl(),
                            friend.getCreatedAt(), friend.getUpdatedAt());
                })
                .toList();
    }

    public boolean areFriends(User user1, User user2) {
        return friendRepository.existsByUserAndFriendAndStatus(user1, user2, FriendStatus.ACCEPTED)
                || friendRepository.existsByUserAndFriendAndStatus(user2, user1, FriendStatus.ACCEPTED);
    }

    //용무 추가
    public List<GetUserResponse> getPendingRequests(User user) {
    return friendRepository.findByFriendAndStatus(user, FriendStatus.PENDING)
            .stream()
            .map(f -> {
                User requester = f.getUser();  // 친구 요청을 보낸 사람
                return new GetUserResponse(
                        requester.getUsername(),
                        requester.getDisplayName(),
                        requester.getEmail(),
                        requester.getProfileImageUrl(),
                        requester.getCreatedAt(),
                        requester.getUpdatedAt()
                );
            })
            .toList();
}
}
