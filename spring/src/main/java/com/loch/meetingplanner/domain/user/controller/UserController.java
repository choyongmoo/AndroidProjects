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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @PreAuthorize("hasRole('ADMIN')") //모든 사용자 조회(관리자 전용)
    public ResponseEntity<List<GetUserResponse>> getAllUsers(
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        List<GetUserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}") //특정 사용자 조회
    public ResponseEntity<GetUserResponse> getUser(
            @PathVariable String username,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        GetUserResponse userResponse = userService.getUserByUsername(username, currentUser.getUser());
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{username}") //사용자 정보 수정(로그인 해야지 쓸 수 있음 한 마디로 jwt 인증된 사람만 정보수정이 가능하다!)
    public ResponseEntity<Void> updateUser(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        userService.updateUser(username, request, currentUser.getUser());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}") //사용자 삭제
    public ResponseEntity<Void> deleteUser(
            @PathVariable String username,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        userService.deleteUser(username, currentUser.getUser());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{username}/location") //사용자 위치 정보 갱신
    public ResponseEntity<Void> updateUserLocation(
            @PathVariable String username,
            @RequestBody UpdateLocationRequest request,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        userService.updateUserLocation(username, request, currentUser.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{username}/upload-profile")
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable String username,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        String imageUrl = userService.saveProfileImage(username, file, currentUser.getUser());
        return ResponseEntity.ok(imageUrl);
    }
}
