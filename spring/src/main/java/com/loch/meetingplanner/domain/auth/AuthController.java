package com.loch.meetingplanner.domain.auth;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loch.meetingplanner.domain.auth.dto.LoginRequest;
import com.loch.meetingplanner.domain.auth.dto.LoginResponse;
import com.loch.meetingplanner.domain.auth.dto.NewPasswordRequest;
import com.loch.meetingplanner.domain.auth.dto.RegisterRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
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

  @PutMapping("/newpassword") // 비밀번호 변경 url /api/auth/newpassword
    public ResponseEntity<Void> newPassword(@RequestBody NewPasswordRequest request) {
        authService.newPassword(request);
        return ResponseEntity.ok().build();
    }
}
