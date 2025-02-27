package com.feedhanjum.back_end.auth.controller.dto.request;

import com.feedhanjum.back_end.auth.domain.PasswordResetToken;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetEmailVerifyRequest(

        @Schema(description = "비밀번호 초기화 토큰을 전송받은 이메일")
        @NotBlank
        @Email
        String email,

        @Schema(description = "비밀번호 초기화 토큰")
        @Size(min = PasswordResetToken.TOKEN_LENGTH, max = PasswordResetToken.TOKEN_LENGTH)
        String code
) {
}
