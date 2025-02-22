package com.feedhanjum.back_end.auth.controller.dto.request;

import com.feedhanjum.back_end.auth.domain.MemberDetails;
import com.feedhanjum.back_end.core.constraints.ByteLength;
import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import com.feedhanjum.back_end.member.domain.ProfileImage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record MemberSignupRequest(
        @Schema(description = "계정 등록에 사용할 이메일 정보. 유일해야 한다.")
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        String email,

        @Schema(description = "계정 등록에 사용할 비밀번호 정보")
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = MemberDetails.MIN_PASSWORD_LENGTH, max = MemberDetails.MAX_PASSWORD_LENGTH, message = "비밀번호는 최소 {min}자 이상, {max}자 이하여야 합니다.")
        String password,

        @Schema(description = "사용자가 이용할 활동명")
        @NotBlank(message = "활동명을 입력해주세요.")
        @ByteLength(min = MemberDetails.MIN_NAME_BYTE, max = MemberDetails.MAX_NAME_BYTE, message = "활동명은 {min}자 이상, {max}자 이하여야 합니다.")
        String name,

        @Schema(description = "사용자가 사용할 프로필 이미지")
        ProfileImage profileImage,

        @NotNull
        @Schema(description = "사용자의 피드백 선호 정보")
        List<FeedbackPreference> feedbackPreferences
) {
}
