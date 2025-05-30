package com.loch.meetingplanner.domain.auth.dto;

public record NewPasswordRequest(String username, String email, String newPassword) {} 
