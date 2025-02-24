package com.feedhanjum.back_end.auth.controller.dto.request;

import com.feedhanjum.back_end.auth.domain.MemberDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "비밀번호 변경 요청")
public record PasswordResetRequest(
        @Schema(description = "이메일")
        @Email
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        String email,

        @Schema(description = "변경할 새로운 비밀번호")
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = MemberDetails.MIN_PASSWORD_LENGTH, max = MemberDetails.MAX_PASSWORD_LENGTH, message = "비밀번호는 최소 {min}자 이상, {max}자 이하여야 합니다.")
        String newPassword
) {
}
