package com.feedhanjum.back_end.team.exception;

public class TeamMembershipNotFoundException extends RuntimeException {
    public TeamMembershipNotFoundException(String message) {
        super(message);
    }
}
