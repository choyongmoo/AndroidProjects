package com.loch.meetingplanner.domain.group.dto;

import com.loch.meetingplanner.domain.group.model.Place;

public record PlaceResponse(
    Long id,
    String name,
    double lat,
    double lng,
    String address
) {
    public static PlaceResponse from(Place place) {
        return new PlaceResponse(
                place.getId(),
                place.getName(),
                place.getLat(),
                place.getLng(),
                place.getAddress()
        );
    }
}
