package com.loch.meetingplanner.common.exception;

public record ErrorResponse(

        int status,

        String message,

        String path) {
}
