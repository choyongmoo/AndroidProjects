package com.loch.meetingplanner.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loch.meetingplanner.domain.user.model.LiveLocation;

@Repository
public interface LiveLocationRepository extends JpaRepository<LiveLocation, Long> {

}
