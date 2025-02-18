package com.feedhanjum.back_end.feedback.event.handler;

import com.feedhanjum.back_end.feedback.service.FeedbackService;
import com.feedhanjum.back_end.team.event.TeamMemberLeftEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveFeedbackRequestHandler {

    private final FeedbackService feedbackService;

    @Async
    @EventListener
    public void on(TeamMemberLeftEvent event) {
        feedbackService.removeFeedbackRequest(event.memberId(), event.teamId());
    }
}
