package com.feedhanjum.back_end.notification.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "여러 알림 읽음 처리 요청")
public record MultipleNotificationReadRequest(
        @Schema(description = "읽음 처리할 알림들의 ID")
        List<Long> notificationIds
) {
}
