package com.feedhanjum.back_end.notification.repository;

import com.feedhanjum.back_end.notification.domain.FeedbackReceiveNotification;
import com.feedhanjum.back_end.notification.domain.InAppNotification;
import com.feedhanjum.back_end.notification.domain.QFeedbackReceiveNotification;
import com.feedhanjum.back_end.notification.domain.QInAppNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class InAppNotificationQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QInAppNotification notification = QInAppNotification.inAppNotification;

    public List<InAppNotification> getInAppNotifications(Long receiverId) {
        return queryFactory.
                select(notification)
                .from(notification)
                .where(notification.receiverId.eq(receiverId))
                .orderBy(notification.id.desc())
                .fetch();
    }

    public List<FeedbackReceiveNotification> getUnreadFeedbackReceiveNotifications(LocalDateTime from, LocalDateTime to) {
        QFeedbackReceiveNotification feedbackNotification = QFeedbackReceiveNotification.feedbackReceiveNotification;
        return queryFactory.select(feedbackNotification)
                .from(feedbackNotification)
                .where(feedbackNotification.isRead.isFalse())
                .where(feedbackNotification.createdAt.between(from, to))
                .fetch();
    }
}