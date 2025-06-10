// 3. domain/location/service/SharedLocationService.java
package com.loch.meetingplanner.domain.location.service;

import com.loch.meetingplanner.domain.location.model.SharedLocation;
import com.loch.meetingplanner.domain.location.repository.SharedLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SharedLocationService {

    private final SharedLocationRepository repository;

    public void updateLocation(String userId, Long appointmentId, double lat, double lng, boolean isSharing) {
        SharedLocation location = repository.findByUserIdAndAppointmentId(userId, appointmentId)
                .orElse(SharedLocation.builder()
                        .userId(userId)
                        .appointmentId(appointmentId)
                        .build());

        location.setLatitude(lat);
        location.setLongitude(lng);
        location.setSharing(isSharing);
        location.setUpdatedAt(LocalDateTime.now());
        repository.save(location);
    }

    public List<SharedLocation> getSharedLocations(Long appointmentId) {
        return repository.findAllByAppointmentIdAndIsSharingTrue(appointmentId);
    }
}