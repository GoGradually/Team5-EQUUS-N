package com.feedhanjum.back_end.feedback.exception;

import com.feedhanjum.back_end.event.EventPublisher;
import com.feedhanjum.back_end.feedback.event.FeedbackRefineCountCompensationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExternalApiControllerAdvice {

    private final EventPublisher eventPublisher;

    public ExternalApiControllerAdvice(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Order(10)
    @ExceptionHandler(ApiResponseFailException.class)
    public ResponseEntity<String> handleApiResponseFailException(ApiResponseFailException e) {
        log.warn("API 호출 실패: ", e.getCause());
        eventPublisher.publishEvent(new FeedbackRefineCountCompensationEvent(e.getCallerId()));
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
    }

    @Order(1)
    @ExceptionHandler(AiRefineChanceAlreadyUsedException.class)
    public ResponseEntity<String> handleAiRefineChanceAlreadyUsedException(AiRefineChanceAlreadyUsedException e) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(e.getMessage());
    }
}
