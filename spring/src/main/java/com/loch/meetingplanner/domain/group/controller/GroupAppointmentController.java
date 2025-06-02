package com.loch.meetingplanner.domain.group.controller;

import com.loch.meetingplanner.domain.appointment.dto.AppointmentResponse;
import com.loch.meetingplanner.domain.appointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupAppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/{groupId}/appointments") //특정 그룹에 포함된 약속 조회
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByGroup(@PathVariable Long groupId) {
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByGroupId(groupId);
        return ResponseEntity.ok(appointments);
    }
}
