package com.feedhanjum.back_end.notification.domain;

import com.feedhanjum.back_end.feedback.domain.Feedback;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue(NotificationType.HEART_REACTION)
public class HeartReactionNotification extends InAppNotification {
    private String senderName;
    private String teamName;

    public HeartReactionNotification(Feedback feedback) {
        super(feedback.getSender().getId());
        this.senderName = feedback.getReceiver().getName();
        this.teamName = feedback.getTeam().getName();
    }
}
