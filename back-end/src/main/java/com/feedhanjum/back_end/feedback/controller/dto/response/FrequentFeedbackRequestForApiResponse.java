package com.feedhanjum.back_end.feedback.controller.dto.response;

import com.feedhanjum.back_end.feedback.domain.FrequentFeedbackRequest;
import com.feedhanjum.back_end.member.controller.dto.MemberDto;

import java.time.LocalDateTime;

public record FrequentFeedbackRequestForApiResponse(
        MemberDto requester,
        Long teamId,
        String requestedContent,
        LocalDateTime createdAt
) {
    public static FrequentFeedbackRequestForApiResponse from(FrequentFeedbackRequest request) {
        return new FrequentFeedbackRequestForApiResponse(new MemberDto(request.getSender()),
                request.getTeam().getId(), request.getRequestedContent(), request.getCreatedAt());
    }
}

