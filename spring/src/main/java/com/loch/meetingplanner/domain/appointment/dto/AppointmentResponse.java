package com.loch.meetingplanner.domain.appointment.dto;

import java.time.LocalDateTime;

import com.loch.meetingplanner.domain.appointment.model.Appointment;

public record AppointmentResponse(

        Long id,

        String groupId,

        String placeId,

        LocalDateTime time,

        String creatorId,

        LocalDateTime createdAt) {
        public static AppointmentResponse fromEntity(Appointment appointment) {
        return new AppointmentResponse(
            appointment.getId(),
            String.valueOf(appointment.getGroup().getId()),
            String.valueOf(appointment.getPlace().getId()),
            appointment.getTime(),
            String.valueOf(appointment.getCreatedBy().getId()),
            appointment.getCreatedAt()
        );
    }

}
