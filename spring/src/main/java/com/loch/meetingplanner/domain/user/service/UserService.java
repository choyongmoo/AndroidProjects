package com.loch.meetingplanner.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loch.meetingplanner.domain.user.dto.UpdateUserRequest;
import com.loch.meetingplanner.domain.user.dto.UserInfoResponse;
import com.loch.meetingplanner.domain.user.model.User;
import com.loch.meetingplanner.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FriendService friendService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FriendService friendService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.friendService = friendService;
    }

    public List<UserInfoResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserInfoResponse(user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public UserInfoResponse getUserByUsername(String username, User currentUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getUsername().equals(currentUser.getUsername())
                && !friendService.areFriends(user, currentUser)) {
            throw new AccessDeniedException("You are not allowed to view this user's information");
        }

        return new UserInfoResponse(user.getUsername(), user.getEmail());
    }

    @Transactional
    public void updateUser(String username, UpdateUserRequest request, User currentUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getUsername().equals(currentUser.getUsername())) {
            throw new AccessDeniedException("You are not allowed to modify this user");
        }

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.email())) {
                throw new IllegalArgumentException("Email already in use");
            }
            user.setEmail(request.email());
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String username, User currentUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getUsername().equals(currentUser.getUsername())) {
            throw new AccessDeniedException("You are not allowed to modify this user");
        }

        userRepository.delete(user);
    }

}
