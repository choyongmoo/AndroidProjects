package com.loch.meetingplanner.domain.user.dto;

import java.time.LocalDateTime;

public record GetUserResponse(

        String username,

        String email,

        String displayName,

        LocalDateTime createdAt,

        LocalDateTime updatedAt) {
}
