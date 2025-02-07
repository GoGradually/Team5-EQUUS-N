package com.feedhanjum.back_end.feedback.controller.dto.response;

import com.feedhanjum.back_end.member.controller.dto.MemberDto;
import com.feedhanjum.back_end.schedule.domain.RegularFeedbackRequest;

import java.time.LocalDateTime;

public record RegularFeedbackRequestForApiResponse(
        MemberDto requester,
        Long scheduleId,
        LocalDateTime createdAt
) {
    public static RegularFeedbackRequestForApiResponse from(RegularFeedbackRequest request) {
        return new RegularFeedbackRequestForApiResponse(new MemberDto(request.getRequester()),
                request.getScheduleMember().getSchedule().getId(), request.getCreatedAt());
    }
}

