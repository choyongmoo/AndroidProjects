package com.loch.meetingplanner.domain.appointment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loch.meetingplanner.config.security.SecurityUserDetails;
import com.loch.meetingplanner.domain.appointment.dto.AppointmentRequest;
import com.loch.meetingplanner.domain.appointment.dto.AppointmentResponse;
import com.loch.meetingplanner.domain.appointment.model.ArrivalLog;
import com.loch.meetingplanner.domain.appointment.service.AppointmentService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/appointments") 
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") //전체 약속 조회
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments(
            @AuthenticationPrincipal SecurityUserDetails currentUser) {
        List<AppointmentResponse> responses = appointmentService.getAllAppointments();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}") //특정 약속 조회
    public ResponseEntity<AppointmentResponse> getAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        AppointmentResponse response = appointmentService.getAppointment(id, currentUser.getUser());
        return ResponseEntity.ok(response);
    }

    @PostMapping // /api/appointments 이거 쓰면 약속 생성됨 
    public ResponseEntity<AppointmentResponse> createAppointment(
            @RequestBody AppointmentRequest appointmentRequest,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        AppointmentResponse response = appointmentService.createAppointment(appointmentRequest, currentUser.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}") //약속 수정
    public ResponseEntity<Void> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentRequest appointmentRequest,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        appointmentService.updateAppointment(id, appointmentRequest, currentUser.getUser());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}") //약속 삭제
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        appointmentService.deleteAppointment(id, currentUser.getUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/arrivals") //특정 약속의 도착 로그 조회
    public ResponseEntity<List<ArrivalLog>> getArrivalLogs(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        List<ArrivalLog> arrivalLogs = appointmentService.getArrivalLogs(id, currentUser.getUser());
        return ResponseEntity.ok(arrivalLogs);
    }

    @PostMapping("/{id}/arrive") //해당 약속에 도착 표시
    public ResponseEntity<Void> arriveAtAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        appointmentService.recordArrival(id, currentUser.getUser());
        return ResponseEntity.ok().build();
    }

        //사용자 id로 약속 찾기
    @GetMapping("/me")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        List<AppointmentResponse> appointments = appointmentService.getAppointmentsForUser(currentUser.getUser());
        return ResponseEntity.ok(appointments);
    } 
}
