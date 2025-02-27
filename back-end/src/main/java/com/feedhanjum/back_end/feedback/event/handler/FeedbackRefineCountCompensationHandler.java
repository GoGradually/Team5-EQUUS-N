package com.feedhanjum.back_end.feedback.event.handler;


import com.feedhanjum.back_end.feedback.event.FeedbackRefineCountCompensationEvent;
import com.feedhanjum.back_end.feedback.service.FeedbackRefineService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedbackRefineCountCompensationHandler {
    private final FeedbackRefineService feedbackRefineService;

    @Async
    @EventListener
    public void on(FeedbackRefineCountCompensationEvent event) {
        feedbackRefineService.compensateRefineCount(event.callerId());
    }
}
