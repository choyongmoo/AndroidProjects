package com.loch.meetingplanner.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(

        @NotBlank String username,

        @NotBlank String email,

        @NotBlank String password,

        @NotBlank String displayName) {
}