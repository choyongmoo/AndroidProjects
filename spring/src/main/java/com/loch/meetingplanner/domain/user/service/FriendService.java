package com.loch.meetingplanner.domain.user.service;

import org.springframework.stereotype.Service;

import com.loch.meetingplanner.domain.user.model.FriendStatus;
import com.loch.meetingplanner.domain.user.model.User;
import com.loch.meetingplanner.domain.user.repository.FriendRepository;

@Service
public class FriendService {

    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public boolean areFriends(User user1, User user2) {
        return friendRepository.existsByUserAndFriendAndStatus(user1, user2, FriendStatus.ACCEPTED)
                || friendRepository.existsByUserAndFriendAndStatus(user2, user1, FriendStatus.ACCEPTED);
    }

}
