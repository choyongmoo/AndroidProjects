package com.loch.meetingplanner.domain.appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loch.meetingplanner.domain.appointment.model.Appointment;
import com.loch.meetingplanner.domain.appointment.model.ArrivalLog;
import com.loch.meetingplanner.domain.user.model.User;

public interface ArrivalLogRepository extends JpaRepository<ArrivalLog, Long> {

    boolean existsByAppointmentAndUser(Appointment appointment, User user);
}