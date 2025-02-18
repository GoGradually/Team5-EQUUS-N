package com.feedhanjum.back_end.notification.repository;

import com.feedhanjum.back_end.notification.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public Optional<FrequentFeedbackRequestNotification> getUnreadFrequentFeedbackRequestNotification(Long receiverId, Long teamId, Long senderId) {
        QFrequentFeedbackRequestNotification requestNotification = QFrequentFeedbackRequestNotification.frequentFeedbackRequestNotification;
        return Optional.ofNullable(queryFactory.select(requestNotification)
                .from(requestNotification)
                .where(requestNotification.isRead.isFalse())
                .where(requestNotification.receiverId.eq(receiverId))
                .where(requestNotification.teamId.eq(teamId))
                .where(requestNotification.senderId.eq(senderId))
                .fetchOne());
    }
}