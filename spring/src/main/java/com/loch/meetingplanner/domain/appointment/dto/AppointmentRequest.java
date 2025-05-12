package com.loch.meetingplanner.domain.appointment.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public record AppointmentRequest(

        @NotBlank String groupId,

        @NotBlank String placeId,

        @NotBlank LocalDateTime time) {
}
