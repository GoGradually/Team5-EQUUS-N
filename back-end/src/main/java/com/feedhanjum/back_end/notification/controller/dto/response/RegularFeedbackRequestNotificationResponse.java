package com.feedhanjum.back_end.notification.controller.dto.response;


import com.feedhanjum.back_end.notification.domain.NotificationType;
import com.feedhanjum.back_end.notification.domain.RegularFeedbackRequestNotification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "정기 피드백 작성 요청 알림")
@Getter
public class RegularFeedbackRequestNotificationResponse extends InAppNotificationDto {
    @Schema(description = "알림 타입", allowableValues = NotificationType.REGULAR_FEEDBACK_REQUEST)
    private final String type = NotificationType.REGULAR_FEEDBACK_REQUEST;
    @Schema(description = "연관된 일정 이름")
    private final String scheduleName;
    @Schema(description = "연관된 일정이 속한 팀 이름")
    private final String teamName;
    @Schema(description = "연관된 일정 ID")
    private final Long scheduleId;
    @Schema(description = "연관된 일정이 속한 팀 ID")
    private final Long teamId;

    @Builder
    public RegularFeedbackRequestNotificationResponse(Long notificationId, Long receiverId, LocalDateTime createdAt, boolean isRead, String scheduleName, String teamName, Long scheduleId, Long teamId) {
        super(notificationId, receiverId, createdAt, isRead);
        this.scheduleName = scheduleName;
        this.teamName = teamName;
        this.scheduleId = scheduleId;
        this.teamId = teamId;
    }

    public static RegularFeedbackRequestNotificationResponse from(RegularFeedbackRequestNotification notification) {
        return builder()
                .notificationId(notification.getId())
                .receiverId(notification.getReceiverId())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.isRead())
                .scheduleId(notification.getScheduleId())
                .scheduleName(notification.getScheduleName())
                .teamId(notification.getTeamId())
                .teamName(notification.getTeamName())
                .build();
    }
}

