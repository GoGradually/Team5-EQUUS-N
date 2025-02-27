package com.feedhanjum.back_end.auth.exception;

public class LoginStateRequiredException extends RuntimeException {
    public LoginStateRequiredException(String message) {
        super(message);
    }
}
