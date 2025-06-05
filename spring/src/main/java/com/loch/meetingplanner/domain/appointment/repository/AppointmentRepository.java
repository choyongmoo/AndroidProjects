package com.loch.meetingplanner.domain.appointment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.loch.meetingplanner.domain.appointment.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByGroupId(Long groupId);

    //사용자 id로 약속 찾기
    @Query("""
    SELECT a
    FROM Appointment a
    WHERE a.group.id IN (
        SELECT gm.group.id
        FROM GroupMember gm
        WHERE gm.user.id = :userId
    )
    """)
    List<Appointment> findAppointmentsByUserId(@Param("userId") Long userId);
}
