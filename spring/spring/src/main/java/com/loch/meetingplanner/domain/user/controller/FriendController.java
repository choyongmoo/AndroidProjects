package com.loch.meetingplanner.domain.user.controller;

import com.loch.meetingplanner.config.security.SecurityUserDetails;
import com.loch.meetingplanner.domain.user.dto.FriendAcceptDto;
import com.loch.meetingplanner.domain.user.dto.FriendRequest;
import com.loch.meetingplanner.domain.user.dto.GetUserResponse;
import com.loch.meetingplanner.domain.user.service.FriendService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    // 1. 친구 요청 보내기
    @PostMapping("/request")
    public ResponseEntity<Void> requestFriend(@AuthenticationPrincipal SecurityUserDetails currentUser,
            @RequestBody @Valid FriendRequest dto) {
        friendService.sendRequest(currentUser.getUser().getUsername(), dto);
        return ResponseEntity.ok().build();
    }

    // 2. 친구 요청 수락
    @PostMapping("/accept")
    public ResponseEntity<Void> acceptRequest(@Valid @RequestBody FriendAcceptDto dto,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {
        friendService.acceptFriendRequest(currentUser.getUser(), dto);
        return ResponseEntity.ok().build();
    }

    // 3. 친구 요청 거절
    public ResponseEntity<Void> rejectRequest(@Valid @RequestBody FriendRequest request,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {
        friendService.rejectFriendRequest(currentUser.getUser(), request.targetUsername());
        return ResponseEntity.ok().build();
    }

    // 4. 친구 목록 조회
    @GetMapping
    public ResponseEntity<List<GetUserResponse>> getFriends(
            @AuthenticationPrincipal SecurityUserDetails currentUser) {
        List<GetUserResponse> friends = friendService.getFriends(currentUser.getUser());
        return ResponseEntity.ok(friends);
    }
}
