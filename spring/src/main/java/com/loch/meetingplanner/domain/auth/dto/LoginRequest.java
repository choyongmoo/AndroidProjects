package com.loch.meetingplanner.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank String username,

        @NotBlank String password) {
}
