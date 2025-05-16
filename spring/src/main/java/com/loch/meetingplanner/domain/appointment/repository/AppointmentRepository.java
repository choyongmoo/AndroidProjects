package com.loch.meetingplanner.domain.appointment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loch.meetingplanner.domain.appointment.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByGroupId(Long groupId);
}
