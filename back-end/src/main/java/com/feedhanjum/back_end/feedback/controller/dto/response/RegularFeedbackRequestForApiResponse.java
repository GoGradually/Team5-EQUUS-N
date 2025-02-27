package com.feedhanjum.back_end.feedback.controller.dto.response;

import com.feedhanjum.back_end.feedback.domain.RegularFeedbackRequest;
import com.feedhanjum.back_end.member.controller.dto.MemberResponse;

import java.time.LocalDateTime;

public record RegularFeedbackRequestForApiResponse(
        MemberResponse requester,
        Long scheduleId,
        LocalDateTime createdAt
) {
    public static RegularFeedbackRequestForApiResponse from(RegularFeedbackRequest request) {
        return new RegularFeedbackRequestForApiResponse(new MemberResponse(request.getRequester()),
                request.getScheduleMember().getSchedule().getId(), request.getCreatedAt());
    }
}

