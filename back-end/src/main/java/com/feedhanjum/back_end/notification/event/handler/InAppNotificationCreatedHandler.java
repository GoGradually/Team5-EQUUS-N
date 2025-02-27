package com.feedhanjum.back_end.notification.event.handler;

import com.feedhanjum.back_end.notification.event.InAppNotificationCreatedEvent;
import com.feedhanjum.back_end.notification.service.InAppNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class InAppNotificationCreatedHandler {
    private final InAppNotificationService inAppNotificationService;

    @Async
    @TransactionalEventListener
    public void on(InAppNotificationCreatedEvent event) {
        inAppNotificationService.sendPushNotification(event.notificationId());
    }
}
