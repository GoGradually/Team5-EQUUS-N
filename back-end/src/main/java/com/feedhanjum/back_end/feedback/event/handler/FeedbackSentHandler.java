package com.feedhanjum.back_end.feedback.event.handler;

import com.feedhanjum.back_end.feedback.event.FeedbackSentEvent;
import com.feedhanjum.back_end.feedback.service.FeedbackRefineService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FeedbackSentHandler {

    private final FeedbackRefineService feedbackRefineService;

    @EventListener
    @Async
    public void on(FeedbackSentEvent event) {
        feedbackRefineService.resetRefineCount(event.senderId());
    }
}
