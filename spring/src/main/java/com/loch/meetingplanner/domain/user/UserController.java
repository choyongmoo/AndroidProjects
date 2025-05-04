package com.loch.meetingplanner.domain.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loch.meetingplanner.domain.user.dto.UpdateUserRequest;
import com.loch.meetingplanner.domain.user.dto.UserInfoResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. 사용자 전체 조회
    @GetMapping
    public ResponseEntity<List<UserInfoResponse>> getAllUsers() {
        List<UserInfoResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // TODO: getUserInfo 및 관련 식별자 userInfo로 변경하기 + username -> userId?
    // 2. 사용자 단건 조회
    @GetMapping("/{username}")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable String username) {
        UserInfoResponse userResponse = userService.getUserInfoByUsername(username);
        return ResponseEntity.ok(userResponse);
    }

    // 3. 사용자 전체 수정
    @PutMapping("/{username}")
    public ResponseEntity<Void> updateUser(@PathVariable String username, @RequestBody UpdateUserRequest request) {

        userService.updateUser(username, request);
        return ResponseEntity.ok().build();
    }

    // 4. 사용자 삭제
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {

        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
