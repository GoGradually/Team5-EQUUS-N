package com.feedhanjum.back_end.auth.exception;

public class PasswordChangeNotAllowedException extends RuntimeException {
    public PasswordChangeNotAllowedException(String message) {
        super(message);
    }
}
