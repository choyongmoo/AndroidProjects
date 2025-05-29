package com.loch.meetingplanner.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loch.meetingplanner.domain.user.dto.UpdateUserRequest;
import com.loch.meetingplanner.domain.user.dto.GetUserResponse;
import com.loch.meetingplanner.domain.user.dto.UpdateLocationRequest;
import com.loch.meetingplanner.domain.user.model.LiveLocation;
import com.loch.meetingplanner.domain.user.model.User;
import com.loch.meetingplanner.domain.user.repository.LiveLocationRepository;
import com.loch.meetingplanner.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LiveLocationRepository liveLocationRepository;
    private final PasswordEncoder passwordEncoder;
    private final FriendService friendService;

    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            FriendService friendService,
            LiveLocationRepository liveLocationRepository) {
        this.userRepository = userRepository;
        this.liveLocationRepository = liveLocationRepository;
        this.passwordEncoder = passwordEncoder;
        this.friendService = friendService;
    }

    public List<GetUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new GetUserResponse(
                        user.getUsername(),
                        user.getDisplayName(),
                        user.getEmail(),
                        user.getProfileImageUrl(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    public GetUserResponse getUserByUsername(String username, User currentUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        boolean isSelf = user.getUsername().equals(currentUser.getUsername());
        boolean isFriend = friendService.areFriends(user, currentUser);

        if (!isSelf && !isFriend) {
            throw new AccessDeniedException("You are not allowed to view this user's information");
        }

        return new GetUserResponse(
                user.getUsername(),
                user.getDisplayName(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    @Transactional
    public void updateUser(String username, UpdateUserRequest request, User currentUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!user.getUsername().equals(currentUser.getUsername())) {
            throw new AccessDeniedException("You are not allowed to modify this user");
        }

        String newEmail = request.email();
        if (newEmail != null && !newEmail.equals(user.getEmail())) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new IllegalArgumentException("Email already in use: " + newEmail);
            }
            user.setEmail(newEmail);
        }

        String newPassword = request.password();
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(newPassword));
        }

        String newDisplayName = request.displayName();
        if (newDisplayName != null && !newDisplayName.isBlank()) {
            user.setDisplayName(newDisplayName);
        }

        //이거 추가해줬어~~
        String newProfileImageUrl = request.profileImageUrl();
        if (newProfileImageUrl != null && !newProfileImageUrl.isBlank()) {
            user.setProfileImageUrl(newProfileImageUrl);
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String username, User currentUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!user.getUsername().equals(currentUser.getUsername())) {
            throw new AccessDeniedException("You are not allowed to delete this user");
        }

        userRepository.delete(user);
    }

    @Transactional
    public void updateUserLocation(String username, UpdateLocationRequest request, User currentUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!user.getUsername().equals(currentUser.getUsername())) {
            throw new AccessDeniedException("You are not allowed to modify this user's location");
        }

        LiveLocation newLocation = new LiveLocation();
        newLocation.setUserId(user.getId());
        newLocation.setLat(request.lat());
        newLocation.setLng(request.lng());

        liveLocationRepository.save(newLocation);
    }
}
