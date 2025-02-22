package com.feedhanjum.back_end.auth.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupEmailSendRequest(

        @Schema(description = "회원가입 인증 토큰을 전송할 이메일")
        @NotBlank
        @Email
        String email
) {
}
