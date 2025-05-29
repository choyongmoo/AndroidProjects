package com.loch.meetingplanner.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record FriendAcceptDto(@NotBlank String requesterUsername) {}
