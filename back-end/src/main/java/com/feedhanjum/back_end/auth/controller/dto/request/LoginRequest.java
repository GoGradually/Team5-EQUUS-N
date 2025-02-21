package com.feedhanjum.back_end.auth.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(description = "사용자가 가입하는데 사용한 이메일")
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        String email,

        @Schema(description = "사용자가 입력한 비밀번호")
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        String password
) {
}
