package com.loch.meetingplanner.domain.user.dto;

import java.time.LocalDateTime;

public record GetUserResponse(

        String username,

        String displayName,

        String email,

        //이거 추가해줬어~~
        String profileImageUrl, 

        LocalDateTime createdAt,

        LocalDateTime updatedAt) {
}
