package com.loch.meetingplanner.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(

        @NotBlank String email,

        @NotBlank String password,

<<<<<<< HEAD
        @NotBlank String displayName,
        
        //이거 추가해줬어~~
        String profileImageUrl) {
=======
        @NotBlank String displayName) {
>>>>>>> fa47ea0963d6fe0e21f66d5f0bb0f4d604da197a
}
