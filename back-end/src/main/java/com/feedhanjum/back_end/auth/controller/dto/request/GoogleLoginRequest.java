package com.feedhanjum.back_end.auth.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;

public record GoogleLoginRequest(
        @Schema(description = "구글측에서 발급받은 인증 관련 코드")
        String code
) {
}
