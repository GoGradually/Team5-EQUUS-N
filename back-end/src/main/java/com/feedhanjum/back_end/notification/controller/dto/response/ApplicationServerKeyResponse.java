package com.feedhanjum.back_end.notification.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "웹 푸시 알림을 위한 Application Server Key를 반환하는 응답")
public record ApplicationServerKeyResponse(
        @Schema(description = "웹 푸시 알림을 위한 Application Server Key")
        String applicationServerKey
) {

}
