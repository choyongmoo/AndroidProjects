package com.loch.meetingplanner.domain.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loch.meetingplanner.config.security.JwtTokenProvider;
import com.loch.meetingplanner.domain.auth.dto.LoginRequest;
import com.loch.meetingplanner.domain.auth.dto.LoginResponse;
import com.loch.meetingplanner.domain.auth.dto.RegisterRequest;
import com.loch.meetingplanner.domain.user.model.User;
import com.loch.meetingplanner.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getUsername());
        return new LoginResponse(user.getUsername(), token);
    }

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(), request.password()));

            String accessToken = jwtTokenProvider.generateToken(auth.getName());
            return new LoginResponse(auth.getName(), accessToken);
        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Invalid username or password");
        } catch (Exception ex) {
            throw new RuntimeException("Login failed");
        }
    }

}
