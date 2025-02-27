package com.feedhanjum.back_end.core.exception;

import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.NonUniqueResultException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(10)
@RestControllerAdvice
@Slf4j
public class InternalErrorControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        log.error("예외 발생!", e);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<String> handleNonUniqueResultException(NonUniqueResultException e) {
        log.error("Non-unique result exception occurred!", e);
        return new ResponseEntity<>("Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(LockTimeoutException.class)
    public ResponseEntity<String> handleLockTimeoutException(LockTimeoutException e) {
        log.error("Lock timeout exception occurred!", e);
        return new ResponseEntity<>("Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockException(ObjectOptimisticLockingFailureException e) {
        log.error("Optimistic lock exception occurred!", e);
        return new ResponseEntity<>("Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
