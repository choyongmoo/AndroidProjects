package com.loch.meetingplanner.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(

        @NotBlank String email,

        @NotBlank String password,

        @NotBlank String displayName,
        
        //이거 추가해줬어~~
        String profileImageUrl) {
}
