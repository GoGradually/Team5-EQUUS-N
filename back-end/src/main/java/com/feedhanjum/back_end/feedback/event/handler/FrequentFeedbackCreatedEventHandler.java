package com.feedhanjum.back_end.feedback.event.handler;

import com.feedhanjum.back_end.feedback.event.FrequentFeedbackCreatedEvent;
import com.feedhanjum.back_end.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class FrequentFeedbackCreatedEventHandler {
    private final FeedbackService feedbackService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void deleteFrequentFeedbackRequest(FrequentFeedbackCreatedEvent event) {
        Long feedbackId = event.feedbackId();
        feedbackService.deleteRelatedFrequentFeedbackRequest(feedbackId);
    }
}
