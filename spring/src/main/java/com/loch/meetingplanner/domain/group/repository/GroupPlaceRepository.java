package com.loch.meetingplanner.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loch.meetingplanner.domain.group.model.Group;
import com.loch.meetingplanner.domain.group.model.GroupPlace;
import com.loch.meetingplanner.domain.group.model.Place;

public interface GroupPlaceRepository extends JpaRepository<GroupPlace, Long> {
    boolean existsByGroupAndPlace(Group group, Place place);
}
