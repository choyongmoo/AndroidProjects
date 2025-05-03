package com.loch.meetingplanner.domain.user;

import org.springframework.stereotype.Service;

import com.loch.meetingplanner.domain.user.dto.UserInfoResponse;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfoResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return new UserInfoResponse(user.getUsername(), user.getEmail());
    }
}
