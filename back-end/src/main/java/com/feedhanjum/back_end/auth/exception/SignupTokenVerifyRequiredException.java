package com.feedhanjum.back_end.auth.exception;

public class SignupTokenVerifyRequiredException extends RuntimeException {
    public SignupTokenVerifyRequiredException(String message) {
        super(message);
    }
}
