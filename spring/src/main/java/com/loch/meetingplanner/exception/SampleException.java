package com.loch.meetingplanner.exception;

public class SampleException extends RuntimeException {
    public SampleException(String message) {
        super("Exception : " + message);
    }

}
