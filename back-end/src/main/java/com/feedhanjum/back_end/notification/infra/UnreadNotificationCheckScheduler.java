package com.feedhanjum.back_end.notification.infra;

import com.feedhanjum.back_end.notification.service.InAppNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UnreadNotificationCheckScheduler {
    private final InAppNotificationService inAppNotificationService;

    @Scheduled(cron = "0 * * * * *")
    public void checkUnreadNotifications() {
        inAppNotificationService.checkUnreadNotifications();
    }
}
