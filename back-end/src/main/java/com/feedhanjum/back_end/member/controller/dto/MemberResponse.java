package com.feedhanjum.back_end.member.controller.dto;

import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.domain.ProfileImage;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberResponse(
        @Schema(description = "회원의 ID")
        Long id,

        @Schema(description = "회원의 활동 이름")
        String name,

        @Schema(description = "회원이 회원가입 시 사용했던 이메일")
        String email,

        @Schema(description = "회원의 프로필 이미지")
        ProfileImage profileImage
) {
    public MemberResponse(Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getProfileImage());
    }
}
