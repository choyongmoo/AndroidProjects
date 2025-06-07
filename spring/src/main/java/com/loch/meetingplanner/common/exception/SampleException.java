package com.loch.meetingplanner.common.exception;

public class SampleException extends RuntimeException {
    public SampleException(String message) {
        super("Exception : " + message);
    }

}
