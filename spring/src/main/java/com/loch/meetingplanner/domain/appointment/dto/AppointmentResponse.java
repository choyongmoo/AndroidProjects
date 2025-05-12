package com.loch.meetingplanner.domain.appointment.dto;

import java.time.LocalDateTime;

public record AppointmentResponse(

        Long id,

        String groupId,

        String placeId,

        LocalDateTime time,

        String creatorId,

        LocalDateTime createdAt) {
}
