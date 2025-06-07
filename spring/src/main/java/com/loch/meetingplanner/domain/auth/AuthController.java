package com.loch.meetingplanner.domain.auth;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loch.meetingplanner.domain.auth.dto.FindIdRequest;
import com.loch.meetingplanner.domain.auth.dto.FindIdResponse;
import com.loch.meetingplanner.domain.auth.dto.LoginRequest;
import com.loch.meetingplanner.domain.auth.dto.LoginResponse;
import com.loch.meetingplanner.domain.auth.dto.NewPasswordRequest;
import com.loch.meetingplanner.domain.auth.dto.RegisterRequest;
import com.loch.meetingplanner.domain.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  public AuthController(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  @PostMapping("/register") //회원가입 url -> /api/auth/register
  public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
    LoginResponse response = authService.register(request);
    URI location = URI.create("/api/users/" + response.username());
    return ResponseEntity.created(location).body(response);
  }

  @PostMapping("/login") //로그인 url -> /api/auth/login
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    LoginResponse response = authService.login(request);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/newpassword") // 전체 URL은 /api/auth/newpassword -> post에서 put으로 바꿈
    public ResponseEntity<Void> newPassword(@RequestBody NewPasswordRequest request) {
        authService.newPassword(request);
        return ResponseEntity.ok().build();
    }

  @PostMapping("/findid") // 아이디 찾기 url /api/auth/findid
  public ResponseEntity<FindIdResponse> findId(@RequestBody FindIdRequest request) {
    String username = userService.findUsernameByEmail(request.email());
    return ResponseEntity.ok(new FindIdResponse(request.email(), username));
  }
}
