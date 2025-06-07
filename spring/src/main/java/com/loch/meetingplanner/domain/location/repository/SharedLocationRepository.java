// 2. domain/location/repository/SharedLocationRepository.java
package com.loch.meetingplanner.domain.location.repository;

import com.loch.meetingplanner.domain.location.model.SharedLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SharedLocationRepository extends JpaRepository<SharedLocation, Long> {
    Optional<SharedLocation> findByUserIdAndAppointmentId(String userId, Long appointmentId);
    List<SharedLocation> findAllByAppointmentIdAndIsSharingTrue(Long appointmentId);
}