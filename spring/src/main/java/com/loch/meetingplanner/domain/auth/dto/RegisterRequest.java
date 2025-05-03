package com.loch.meetingplanner.domain.auth.dto;

public record RegisterRequest(String username, String email, String password) {
}