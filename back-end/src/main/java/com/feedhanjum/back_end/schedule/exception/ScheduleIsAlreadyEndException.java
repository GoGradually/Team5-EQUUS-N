package com.feedhanjum.back_end.schedule.exception;

public class ScheduleIsAlreadyEndException extends RuntimeException {
    public ScheduleIsAlreadyEndException(String message) {
        super(message);
    }
}
