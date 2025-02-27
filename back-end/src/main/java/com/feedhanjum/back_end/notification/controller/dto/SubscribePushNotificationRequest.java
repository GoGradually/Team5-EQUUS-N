package com.feedhanjum.back_end.notification.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import nl.martijndwars.webpush.Subscription;

@Schema(description = "푸시 알림 구독 요청")
public record SubscribePushNotificationRequest(
        @Schema(description = "브라우저에서 발급해준 웹 푸시 구독정보")
        @NotNull
        Subscription subscription) {
}
