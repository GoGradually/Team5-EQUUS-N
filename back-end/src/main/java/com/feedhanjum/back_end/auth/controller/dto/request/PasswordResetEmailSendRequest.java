package com.feedhanjum.back_end.auth.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetEmailSendRequest(

        @Schema(description = "비밀번호 초기화 토큰을 전송할 이메일")
        @NotBlank
        @Email
        String email
) {
}
