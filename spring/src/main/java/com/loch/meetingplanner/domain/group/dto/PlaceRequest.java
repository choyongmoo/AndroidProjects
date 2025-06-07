package com.loch.meetingplanner.domain.group.dto;

public record PlaceRequest(
    String name,
    double lat,
    double lng,
    String address
) {}
