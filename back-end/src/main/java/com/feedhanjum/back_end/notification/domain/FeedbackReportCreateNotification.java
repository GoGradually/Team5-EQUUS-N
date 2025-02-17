package com.feedhanjum.back_end.notification.domain;

import com.feedhanjum.back_end.member.domain.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue(NotificationType.FEEDBACK_REPORT_CREATE)
public class FeedbackReportCreateNotification extends InAppNotification {
    private String receiverName;

    public FeedbackReportCreateNotification(Member receiver) {
        super(receiver.getId());
        this.receiverName = receiver.getName();
    }
}
