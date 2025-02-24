package com.feedhanjum.back_end.notification.controller.dto.response;

import com.feedhanjum.back_end.notification.domain.FrequentFeedbackRequestNotification;
import com.feedhanjum.back_end.notification.domain.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "수시 피드백 요청 알림")
@Getter
public class FrequentFeedbackRequestNotificationResponse extends InAppNotificationDto {
    @Schema(description = "알림 타입", allowableValues = NotificationType.FREQUENT_FEEDBACK_REQUEST)
    private final String type = NotificationType.FREQUENT_FEEDBACK_REQUEST;
    @Schema(description = "요청자 이름")
    private final String senderName;
    @Schema(description = "팀 id")
    private final Long teamId;
    @Schema(description = "요청자 id")
    private final Long senderId;

    @Builder
    public FrequentFeedbackRequestNotificationResponse(Long notificationId, Long receiverId, LocalDateTime createdAt, boolean isRead, String senderName, Long teamId, Long senderId) {
        super(notificationId, receiverId, createdAt, isRead);
        this.senderName = senderName;
        this.teamId = teamId;
        this.senderId = senderId;
    }

    public static FrequentFeedbackRequestNotificationResponse from(FrequentFeedbackRequestNotification notification) {
        return builder()
                .notificationId(notification.getId())
                .receiverId(notification.getReceiverId())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.isRead())
                .senderName(notification.getSenderName())
                .teamId(notification.getTeamId())
                .senderId(notification.getSenderId())
                .build();
    }
}
