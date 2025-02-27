package com.feedhanjum.back_end.notification.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.feedhanjum.back_end.notification.domain.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FeedbackReceiveNotificationResponse.class, name = NotificationType.FEEDBACK_RECEIVE),
        @JsonSubTypes.Type(value = FeedbackReportCreateNotificationResponse.class, name = NotificationType.FEEDBACK_REPORT_CREATE),
        @JsonSubTypes.Type(value = FrequentFeedbackRequestNotificationResponse.class, name = NotificationType.FREQUENT_FEEDBACK_REQUEST),
        @JsonSubTypes.Type(value = HeartReactionNotificationResponse.class, name = NotificationType.HEART_REACTION),
        @JsonSubTypes.Type(value = RegularFeedbackRequestNotificationResponse.class, name = NotificationType.REGULAR_FEEDBACK_REQUEST),
        @JsonSubTypes.Type(value = ScheduleCreateNotificationResponse.class, name = NotificationType.SCHEDULE_CREATE),
        @JsonSubTypes.Type(value = TeamLeaderChangeNotificationResponse.class, name = NotificationType.TEAM_LEADER_CHANGE),
        @JsonSubTypes.Type(value = UnreadFeedbackExistNotificationResponse.class, name = NotificationType.UNREAD_FEEDBACK_EXIST)
})
@Schema(subTypes = {FeedbackReceiveNotificationResponse.class, FeedbackReportCreateNotificationResponse.class, FrequentFeedbackRequestNotificationResponse.class, HeartReactionNotificationResponse.class, RegularFeedbackRequestNotificationResponse.class, ScheduleCreateNotificationResponse.class, TeamLeaderChangeNotificationResponse.class, UnreadFeedbackExistNotificationResponse.class})
@Getter
public abstract class InAppNotificationDto {
    @Schema(description = "알림 ID")
    protected final Long notificationId;
    @Schema(description = "받는 사람 ID")
    protected final Long receiverId;
    @Schema(description = "알림 생성 시간")
    protected final LocalDateTime createdAt;
    @Schema(description = "읽음 여부")
    protected final boolean isRead;

    protected InAppNotificationDto(Long notificationId, Long receiverId, LocalDateTime createdAt, boolean isRead) {
        this.notificationId = notificationId;
        this.receiverId = receiverId;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public static InAppNotificationDto from(InAppNotification inAppNotification) {
        if (inAppNotification instanceof FeedbackReceiveNotification n) {
            return FeedbackReceiveNotificationResponse.from(n);
        } else if (inAppNotification instanceof FeedbackReportCreateNotification n) {
            return FeedbackReportCreateNotificationResponse.from(n);
        } else if (inAppNotification instanceof FrequentFeedbackRequestNotification n) {
            return FrequentFeedbackRequestNotificationResponse.from(n);
        } else if (inAppNotification instanceof HeartReactionNotification n) {
            return HeartReactionNotificationResponse.from(n);
        } else if (inAppNotification instanceof RegularFeedbackRequestNotification n) {
            return RegularFeedbackRequestNotificationResponse.from(n);
        } else if (inAppNotification instanceof ScheduleCreateNotification n) {
            return ScheduleCreateNotificationResponse.from(n);
        } else if (inAppNotification instanceof TeamLeaderChangeNotification n) {
            return TeamLeaderChangeNotificationResponse.from(n);
        } else if (inAppNotification instanceof UnreadFeedbackExistNotification n) {
            return UnreadFeedbackExistNotificationResponse.from(n);
        }
        throw new RuntimeException("Unreachable");
    }
}
