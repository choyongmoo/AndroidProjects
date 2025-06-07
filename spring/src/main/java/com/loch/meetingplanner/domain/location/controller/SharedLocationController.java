// 4. domain/location/controller/SharedLocationController.java
package com.loch.meetingplanner.domain.location.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loch.meetingplanner.domain.location.model.SharedLocation;
import com.loch.meetingplanner.domain.location.service.SharedLocationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class SharedLocationController {

    private final SharedLocationService service;

    @PostMapping("/share")
    public void shareLocation(@RequestBody LocationRequest request) {
        service.updateLocation(request.getUserId(), request.getAppointmentId(),
                request.getLatitude(), request.getLongitude(), request.isSharing());
    }

    @GetMapping("/shared/{appointmentId}")
    public List<SharedLocation> getShared(@PathVariable Long appointmentId) {
        return service.getSharedLocations(appointmentId);
    }

    @Data
@AllArgsConstructor
@NoArgsConstructor
public static class LocationRequest {
    private String userId;
    private Long appointmentId;
    private double latitude;
    private double longitude;

    @JsonProperty("isSharing") // 🔥 요거 추가!
    private boolean isSharing;
}
}