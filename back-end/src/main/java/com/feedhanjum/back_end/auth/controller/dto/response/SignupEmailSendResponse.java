package com.feedhanjum.back_end.auth.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SignupEmailSendResponse(
        @Schema(description = "회원가입 인증 토큰 유효기간")
        @NotNull
        @Future
        LocalDateTime validUntil
) {
}
