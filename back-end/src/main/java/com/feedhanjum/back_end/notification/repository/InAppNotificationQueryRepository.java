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
                selectFrom(notification)
                .where(notification.receiverId.eq(receiverId))
                .orderBy(notification.id.desc())
                .fetch();
    }

    public List<FeedbackReceiveNotification> getUnreadFeedbackReceiveNotifications(LocalDateTime from, LocalDateTime to) {
        QFeedbackReceiveNotification feedbackNotification = QFeedbackReceiveNotification.feedbackReceiveNotification;
        return queryFactory
                .selectFrom(feedbackNotification)
                .where(feedbackNotification.isRead.isFalse())
                .where(feedbackNotification.createdAt.between(from, to))
                .fetch();
    }

    public Optional<FrequentFeedbackRequestNotification> getUnreadFrequentFeedbackRequestNotification(Long receiverId, Long teamId, Long senderId) {
        QFrequentFeedbackRequestNotification requestNotification = QFrequentFeedbackRequestNotification.frequentFeedbackRequestNotification;
        return Optional.ofNullable(queryFactory
                .selectFrom(requestNotification)
                .where(requestNotification.isRead.isFalse())
                .where(requestNotification.receiverId.eq(receiverId))
                .where(requestNotification.teamId.eq(teamId))
                .where(requestNotification.senderId.eq(senderId))
                .fetchFirst());
    }

    public Optional<UnreadFeedbackExistNotification> getLatestUnreadFeedbackExistNotification(Long receiverId) {
        QUnreadFeedbackExistNotification unreadNotification = QUnreadFeedbackExistNotification.unreadFeedbackExistNotification;
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(unreadNotification)
                        .where(unreadNotification.receiverId.eq(receiverId))
                        .orderBy(unreadNotification.id.desc())
                        .fetchFirst()
        );
    }
}