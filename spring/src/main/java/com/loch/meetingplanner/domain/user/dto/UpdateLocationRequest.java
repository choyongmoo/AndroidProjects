package com.loch.meetingplanner.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateLocationRequest(

        @NotBlank double lat,

        @NotBlank double lng) {

}
