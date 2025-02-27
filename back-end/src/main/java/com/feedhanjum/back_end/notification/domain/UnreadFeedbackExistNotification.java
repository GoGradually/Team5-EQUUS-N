package com.feedhanjum.back_end.notification.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue(NotificationType.UNREAD_FEEDBACK_EXIST)
public class UnreadFeedbackExistNotification extends InAppNotification {
    private String senderName;
    private String teamName;
    private Long teamId;

    public UnreadFeedbackExistNotification(FeedbackReceiveNotification notification) {
        super(notification.getReceiverId());
        this.senderName = notification.getSenderName();
        this.teamName = notification.getTeamName();
        this.teamId = notification.getTeamId();
    }
}
