package com.loch.meetingplanner.domain.appointment.dto;

import java.time.LocalDateTime;

import com.loch.meetingplanner.domain.appointment.model.Appointment;

public record AppointmentResponse(

        Long id,

        String groupId,

        String placeId,

        String placeName, 

        LocalDateTime time,

        String creatorId,

        LocalDateTime createdAt,
        
        Integer penalty,

        String title

) {
    public static AppointmentResponse fromEntity(Appointment appointment) {
        return new AppointmentResponse(
            appointment.getId(),
            String.valueOf(appointment.getGroup().getId()),
            String.valueOf(appointment.getPlace().getId()),
            appointment.getPlace().getName(),
            appointment.getTime(),
            String.valueOf(appointment.getCreatedBy().getId()),
            appointment.getCreatedAt(),
            appointment.getPenalty(),
            appointment.getTitle()
        );
    }
}
