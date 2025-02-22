package com.feedhanjum.back_end.member.controller.dto;

import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import com.feedhanjum.back_end.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MemberFeedbackPreferenceResponse(
        @Schema(description = "회원의 활동 이름")
        String name,

        @Schema(description = "회원의 피드백 선호 정보")
        List<FeedbackPreference> feedbackPreferences
) {
    public MemberFeedbackPreferenceResponse(Member member) {
        this(member.getName(), member.getFeedbackPreferences().stream().toList());
    }
}
