package com.loch.meetingplanner.domain.appointment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loch.meetingplanner.config.security.SecurityUserDetails;
import com.loch.meetingplanner.domain.appointment.dto.AppointmentRequest;
import com.loch.meetingplanner.domain.appointment.dto.AppointmentResponse;
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
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments(
            @AuthenticationPrincipal SecurityUserDetails currentUser) {
        List<AppointmentResponse> responses = appointmentService.getAllAppointments();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        AppointmentResponse response = appointmentService.getAppointment(id, currentUser.getUser());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/new")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @RequestBody AppointmentRequest appointmentRequest,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        AppointmentResponse response = appointmentService.createAppointment(appointmentRequest, currentUser.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        appointmentService.deleteAppointment(id, currentUser.getUser());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/arrive")
    public ResponseEntity<Void> arriveAtAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityUserDetails currentUser) {

        appointmentService.recordArrival(id, currentUser.getUser());
        return ResponseEntity.ok().build();
    }
}
