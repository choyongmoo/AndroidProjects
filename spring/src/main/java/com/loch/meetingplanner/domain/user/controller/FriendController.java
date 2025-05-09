// package com.loch.meetingplanner.domain.user.controller;

// import com.loch.meetingplanner.config.security.SecurityUserDetails;
// import com.loch.meetingplanner.domain.user.dto.FriendRequest;
// import com.loch.meetingplanner.domain.user.dto.UserInfoResponse;
// import com.loch.meetingplanner.domain.user.service.FriendService;

// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;

// import java.util.List;

// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/friends")
// @PreAuthorize("hasRole('USER')")
// public class FriendController {

// private final FriendService friendService;

// public FriendController(FriendService friendService) {
// this.friendService = friendService;
// }

// // 1. 친구 요청 보내기
// @PostMapping("/request")
// public ResponseEntity<Void> sendRequest(
// @Valid @RequestBody FriendRequest request,
// @AuthenticationPrincipal SecurityUserDetails currentUser) {
// friendService.sendFriendRequest(currentUser.getUser(),
// request.getTargetUsername());
// return ResponseEntity.ok().build();
// }

// // 2. 친구 요청 수락
// @PostMapping("/accept")
// public ResponseEntity<Void> acceptRequest(
// @Valid @RequestBody FriendRequest request,
// @AuthenticationPrincipal SecurityUserDetails currentUser) {
// friendService.acceptFriendRequest(currentUser.getUser(),
// request.getTargetUsername());
// return ResponseEntity.ok().build();
// }

// // 3. 친구 요청 거절
// @PostMapping("/reject")
// public ResponseEntity<Void> rejectRequest(
// @Valid @RequestBody FriendRequest request,
// @AuthenticationPrincipal SecurityUserDetails currentUser) {
// friendService.rejectFriendRequest(currentUser.getUser(),
// request.getTargetUsername());
// return ResponseEntity.ok().build();
// }

// // 4. 친구 목록 조회
// @GetMapping
// public ResponseEntity<List<UserInfoResponse>> getFriends(
// @AuthenticationPrincipal SecurityUserDetails currentUser) {
// List<UserInfoResponse> friends =
// friendService.getFriends(currentUser.getUser());
// return ResponseEntity.ok(friends);
// }
// }