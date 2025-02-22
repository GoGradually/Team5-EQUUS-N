package com.feedhanjum.back_end.notification.controller.dto.response;

import com.feedhanjum.back_end.notification.domain.NotificationType;
import com.feedhanjum.back_end.notification.domain.ScheduleCreateNotification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "일정 생성 알림")
@Getter
public class ScheduleCreateNotificationResponse extends InAppNotificationDto {
    @Schema(description = "알림 타입", allowableValues = NotificationType.SCHEDULE_CREATE)
    private final String type = NotificationType.SCHEDULE_CREATE;
    @Schema(description = "생성된 일정이 속한 팀 이름")
    private final String teamName;
    @Schema(description = "생성된 일정의 날짜")
    private final LocalDateTime scheduleDate;
    @Schema(description = "생성된 일정의 팀 ID")
    private final Long teamId;

    @Builder
    public ScheduleCreateNotificationResponse(Long notificationId, Long receiverId, LocalDateTime createdAt, boolean isRead, String teamName, LocalDateTime scheduleDate, Long teamId) {
        super(notificationId, receiverId, createdAt, isRead);
        this.teamName = teamName;
        this.scheduleDate = scheduleDate;
        this.teamId = teamId;
    }

    public static ScheduleCreateNotificationResponse from(ScheduleCreateNotification notification) {
        return builder()
                .notificationId(notification.getId())
                .receiverId(notification.getReceiverId())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.isRead())
                .teamId(notification.getTeamId())
                .teamName(notification.getTeamName())
                .scheduleDate(notification.getScheduleDate())
                .build();

    }
}
