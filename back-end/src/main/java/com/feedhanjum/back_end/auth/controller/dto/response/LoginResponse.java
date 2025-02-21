package com.feedhanjum.back_end.auth.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "로그인 응답 메시지")
        String message,
        @Schema(description = "사용자의 인조 DB ID 구분값")
        Long userId,
        @Schema(description = "사용자의 이메일 정보")
        String email
) {
}
