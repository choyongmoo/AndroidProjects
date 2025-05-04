package com.loch.meetingplanner.domain.user.dto;

public record UpdateUserRequest(String username, String email, String password) {
}
