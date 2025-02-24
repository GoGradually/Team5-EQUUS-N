package com.feedhanjum.back_end.feedback.controller.dto.response;

import com.feedhanjum.back_end.feedback.domain.FrequentFeedbackRequest;
import com.feedhanjum.back_end.member.controller.dto.MemberResponse;

import java.time.LocalDateTime;

public record FrequentFeedbackRequestForApiResponse(
        MemberResponse requester,
        Long teamId,
        String requestedContent,
        LocalDateTime createdAt
) {
    public static FrequentFeedbackRequestForApiResponse from(FrequentFeedbackRequest request) {
        return new FrequentFeedbackRequestForApiResponse(new MemberResponse(request.getSender()),
                request.getTeam().getId(), request.getRequestedContent(), request.getCreatedAt());
    }
}

