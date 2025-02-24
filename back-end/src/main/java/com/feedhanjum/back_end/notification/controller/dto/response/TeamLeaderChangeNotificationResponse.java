package com.feedhanjum.back_end.notification.controller.dto.response;

import com.feedhanjum.back_end.notification.domain.NotificationType;
import com.feedhanjum.back_end.notification.domain.TeamLeaderChangeNotification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "팀장이 나로 변경되었음을 알리는 알림")
@Getter
public class TeamLeaderChangeNotificationResponse extends InAppNotificationDto {
    @Schema(description = "알림 타입", allowableValues = NotificationType.TEAM_LEADER_CHANGE)
    private final String type = NotificationType.TEAM_LEADER_CHANGE;
    @Schema(description = "팀장을 맡게 된 팀 이름")
    private final String teamName;
    @Schema(description = "팀장을 맡게 된 팀 ID")
    private final Long teamId;

    @Builder
    public TeamLeaderChangeNotificationResponse(Long notificationId, Long receiverId, LocalDateTime createdAt, boolean isRead, String teamName, Long teamId) {
        super(notificationId, receiverId, createdAt, isRead);
        this.teamName = teamName;
        this.teamId = teamId;
    }

    public static TeamLeaderChangeNotificationResponse from(TeamLeaderChangeNotification notification) {
        return builder()
                .notificationId(notification.getId())
                .receiverId(notification.getReceiverId())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.isRead())
                .teamName(notification.getTeamName())
                .teamId(notification.getTeamId())
                .build();
    }
}
