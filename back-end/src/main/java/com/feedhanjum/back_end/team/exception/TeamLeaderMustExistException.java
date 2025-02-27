package com.feedhanjum.back_end.team.exception;

public class TeamLeaderMustExistException extends RuntimeException {
    public TeamLeaderMustExistException(String message) {
        super(message);
    }
}
