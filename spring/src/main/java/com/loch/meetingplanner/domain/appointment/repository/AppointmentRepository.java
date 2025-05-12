package com.loch.meetingplanner.domain.appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loch.meetingplanner.domain.appointment.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
