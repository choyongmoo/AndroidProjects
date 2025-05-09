package com.loch.meetingplanner.domain.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loch.meetingplanner.config.security.SecurityUserDetails;
import com.loch.meetingplanner.domain.user.dto.UpdateUserRequest;
import com.loch.meetingplanner.domain.user.dto.UserInfoResponse;
import com.loch.meetingplanner.domain.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. 사용자 전체 조회
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserInfoResponse>> getAllUsers(
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        List<UserInfoResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 2. 사용자 단건 조회
    @GetMapping("/{username}")
    public ResponseEntity<UserInfoResponse> getUser(
            @PathVariable String username,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        UserInfoResponse userResponse = userService.getUserByUsername(username, currentUser.getUser());
        return ResponseEntity.ok(userResponse);
    }

    // 3. 사용자 전체 수정
    @PutMapping("/{username}")
    public ResponseEntity<Void> updateUser(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        userService.updateUser(username, request, currentUser.getUser());
        return ResponseEntity.ok().build();
    }

    // 4. 사용자 삭제
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String username,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        userService.deleteUser(username, currentUser.getUser());
        return ResponseEntity.noContent().build();
    }
}
