package com.loch.meetingplanner.domain.user.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    member,
    MEMBER,
    USER,
    ADMIN;

    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}
