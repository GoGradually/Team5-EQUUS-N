package com.feedhanjum.back_end.feedback.exception;

import lombok.Getter;

@Getter
public class ApiResponseFailException extends RuntimeException {
    private Long callerId;

    public ApiResponseFailException(String message, Long callerId, Throwable cause) {
        super(message, cause);
        this.callerId = callerId;
    }
}
