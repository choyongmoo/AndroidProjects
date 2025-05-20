package com.loch.meetingplanner.domain.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loch.meetingplanner.config.security.SecurityUserDetails;
import com.loch.meetingplanner.domain.user.dto.UpdateUserRequest;
import com.loch.meetingplanner.domain.user.dto.GetUserResponse;
import com.loch.meetingplanner.domain.user.dto.UpdateLocationRequest;
import com.loch.meetingplanner.domain.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<GetUserResponse>> getAllUsers(
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        List<GetUserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<GetUserResponse> getUser(
            @PathVariable String username,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        GetUserResponse userResponse = userService.getUserByUsername(username, currentUser.getUser());
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{username}")
    public ResponseEntity<Void> updateUser(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        userService.updateUser(username, request, currentUser.getUser());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String username,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        userService.deleteUser(username, currentUser.getUser());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{username}/location")
    public ResponseEntity<Void> updateUserLocation(
            @PathVariable String username,
            @RequestBody UpdateLocationRequest request,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        userService.updateUserLocation(username, request, currentUser.getUser());
        return ResponseEntity.ok().build();
    }
}
