package com.feedhanjum.back_end.auth.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberSignupResponse(
        @Schema(description = "사용자에게 할당된 사용자의 인조 ID")
        Long id,
        @Schema(description = "사용자를 회원 등록하는데 사용한 이메일")
        String email,
        @Schema(description = "회원가입 응답 메시지")
        String message
) {
}
