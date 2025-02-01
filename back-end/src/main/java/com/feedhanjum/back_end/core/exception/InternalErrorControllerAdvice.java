package com.feedhanjum.back_end.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(2)
@RestControllerAdvice
@Slf4j
public class InternalErrorControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
