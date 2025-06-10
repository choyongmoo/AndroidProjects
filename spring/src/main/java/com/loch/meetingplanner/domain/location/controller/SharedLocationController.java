package com.loch.meetingplanner.domain.location.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loch.meetingplanner.domain.location.model.SharedLocation;
import com.loch.meetingplanner.domain.location.service.SharedLocationService;
import com.loch.meetingplanner.domain.user.model.User;
import com.loch.meetingplanner.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class SharedLocationController {

    private final SharedLocationService service;
    private final UserRepository userRepository;

    @PostMapping("/share")
    public void shareLocation(@RequestBody LocationRequest request) {
        service.updateLocation(request.getUserId(), request.getAppointmentId(),
                request.getLatitude(), request.getLongitude(), request.isSharing());
    }

    @GetMapping("/shared/{appointmentId}")
    public List<SharedLocationResponse> getShared(@PathVariable Long appointmentId) {
        List<SharedLocation> locations = service.getSharedLocations(appointmentId);

        return locations.stream().map(loc -> {
            String name = userRepository.findByUsername(loc.getUserId())
                    .map(User::getDisplayName)
                    .orElse(loc.getUserId());
            return new SharedLocationResponse( loc.getUserId(),name, loc.getAppointmentId(), loc.getLatitude(), loc.getLongitude(), loc.isSharing());
        }).collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LocationRequest {
        private String userId;
        private Long appointmentId;
        private double latitude;
        private double longitude;

        @JsonProperty("isSharing")
        private boolean isSharing;
    }

    @Data
    @AllArgsConstructor
    public static class SharedLocationResponse {
        private String userId; // ✅ 추가
        private String name;
        private Long appointmentId;
        private double latitude;
        private double longitude;
        private boolean isSharing;
    }
}
