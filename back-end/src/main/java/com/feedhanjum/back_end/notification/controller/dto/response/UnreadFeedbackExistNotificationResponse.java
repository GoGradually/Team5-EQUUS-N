package com.feedhanjum.back_end.notification.controller.dto.response;

import com.feedhanjum.back_end.notification.domain.NotificationType;
import com.feedhanjum.back_end.notification.domain.UnreadFeedbackExistNotification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "미확인 피드백 존재 알림")
@Getter
public class UnreadFeedbackExistNotificationResponse extends InAppNotificationDto {
    @Schema(description = "알림 타입", allowableValues = NotificationType.UNREAD_FEEDBACK_EXIST)
    private final String type = NotificationType.UNREAD_FEEDBACK_EXIST;
    @Schema(description = "미확인 피드백의 발송자 이름")
    private final String senderName;
    @Schema(description = "미확인 피드백의 팀 이름")
    private final String teamName;
    @Schema(description = "미확인 피드백의 팀 ID")
    private final Long teamId;

    @Builder
    public UnreadFeedbackExistNotificationResponse(Long notificationId, Long receiverId, LocalDateTime createdAt, boolean isRead, String senderName, String teamName, Long teamId) {
        super(notificationId, receiverId, createdAt, isRead);
        this.senderName = senderName;
        this.teamName = teamName;
        this.teamId = teamId;
    }

    public static UnreadFeedbackExistNotificationResponse from(UnreadFeedbackExistNotification notification) {
        return builder()
                .notificationId(notification.getId())
                .receiverId(notification.getReceiverId())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.isRead())
                .senderName(notification.getSenderName())
                .teamName(notification.getTeamName())
                .teamId(notification.getTeamId())
                .build();
    }
}
