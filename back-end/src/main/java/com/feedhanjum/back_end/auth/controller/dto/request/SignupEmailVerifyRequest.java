package com.feedhanjum.back_end.auth.controller.dto.request;

import com.feedhanjum.back_end.auth.domain.EmailSignupToken;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupEmailVerifyRequest(

        @Schema(description = "회원가입 인증 토큰을 전송받은 이메일")
        @NotBlank
        @Email
        String email,

        @Schema(description = "회원가입 인증 토큰")
        @Size(min = EmailSignupToken.TOKEN_LENGTH, max = EmailSignupToken.TOKEN_LENGTH)
        String code
) {
}
