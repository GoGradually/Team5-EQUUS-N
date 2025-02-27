package com.feedhanjum.back_end.notification.controller.dto.response;

import com.feedhanjum.back_end.notification.domain.HeartReactionNotification;
import com.feedhanjum.back_end.notification.domain.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "피드백 하트 반응 알림")
@Getter
public class HeartReactionNotificationResponse extends InAppNotificationDto {
    @Schema(description = "알림 타입", allowableValues = NotificationType.HEART_REACTION)
    private final String type = NotificationType.HEART_REACTION;
    @Schema(description = "보낸 사람 이름")
    private final String senderName;
    @Schema(description = "팀 이름")
    private final String teamName;

    @Builder
    public HeartReactionNotificationResponse(Long notificationId, Long receiverId, LocalDateTime createdAt, boolean isRead, String senderName, String teamName) {
        super(notificationId, receiverId, createdAt, isRead);
        this.senderName = senderName;
        this.teamName = teamName;
    }

    public static HeartReactionNotificationResponse from(HeartReactionNotification notification) {
        return builder()
                .notificationId(notification.getId())
                .receiverId(notification.getReceiverId())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.isRead())
                .senderName(notification.getSenderName())
                .teamName(notification.getTeamName())
                .build();
    }
}
