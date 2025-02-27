package com.feedhanjum.back_end.notification.event.handler;

import com.feedhanjum.back_end.notification.service.InAppNotificationService;
import com.feedhanjum.back_end.team.event.TeamLeaderChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class TeamLeaderChangedHandler {
    private final InAppNotificationService inAppNotificationService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(TeamLeaderChangedEvent event) {
        inAppNotificationService.createNotification(event);
    }
}
