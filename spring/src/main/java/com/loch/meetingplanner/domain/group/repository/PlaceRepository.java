package com.loch.meetingplanner.domain.group.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loch.meetingplanner.domain.group.model.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    // 위도/경도/주소/이름이 모두 같은 장소 있는지 확인
    Optional<Place> findByNameAndLatAndLngAndAddress(String name, double lat, double lng, String address);
}