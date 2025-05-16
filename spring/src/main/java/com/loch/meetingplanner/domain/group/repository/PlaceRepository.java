package com.loch.meetingplanner.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loch.meetingplanner.domain.group.model.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
