package com.loch.meetingplanner.domain.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loch.meetingplanner.config.security.JwtTokenProvider;
import com.loch.meetingplanner.config.security.SecurityUserDetails;
import com.loch.meetingplanner.domain.auth.dto.LoginRequest;
import com.loch.meetingplanner.domain.auth.dto.LoginResponse;
import com.loch.meetingplanner.domain.auth.dto.NewPasswordRequest;
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
            throw new IllegalArgumentException("Username already exists: " + request.username());
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists: " + request.email());
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setDisplayName(request.displayName());

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user);
        return new LoginResponse(user.getUsername(), token);
    }

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(), request.password()));

            SecurityUserDetails userDetails = (SecurityUserDetails) auth.getPrincipal();
            String token = jwtTokenProvider.generateToken(userDetails.getUser());

            return new LoginResponse(auth.getName(), token);

        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid username or password");
        } catch (Exception e) {
            throw new RuntimeException("Login failed due to an internal error");
        }
    }

    @Transactional
    public void newPassword(NewPasswordRequest request) {
        User user = userRepository.findByUsername(request.username())
                .filter(u -> u.getEmail().equals(request.email()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or email"));

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
