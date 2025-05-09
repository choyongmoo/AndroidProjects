package com.loch.meetingplanner.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;

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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserInfoResponse getUserInfoByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return new UserInfoResponse(user.getUsername(), user.getEmail());
    }

    public List<UserInfoResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserInfoResponse(user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUser(String username, UpdateUserRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        userRepository.save(user); // 변경사항 반영
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        userRepository.delete(user);
    }

}
