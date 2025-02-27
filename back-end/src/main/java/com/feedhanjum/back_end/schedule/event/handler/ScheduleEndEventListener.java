package com.feedhanjum.back_end.schedule.event.handler;

import com.feedhanjum.back_end.feedback.service.FeedbackService;
import com.feedhanjum.back_end.schedule.event.ScheduleEndedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class ScheduleEndEventListener {
    private final FeedbackService feedbackService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ScheduleEndedEvent event) {
        Long scheduleId = event.scheduleId();
        feedbackService.createRegularFeedbackRequests(scheduleId);
    }
}
