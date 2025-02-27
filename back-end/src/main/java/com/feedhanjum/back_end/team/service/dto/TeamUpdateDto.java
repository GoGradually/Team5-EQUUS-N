package com.feedhanjum.back_end.team.service.dto;

import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.team.controller.dto.TeamUpdateRequest;

import java.time.LocalDate;

public record TeamUpdateDto(
        String teamName,
        LocalDate startDate,
        LocalDate endDate,
        FeedbackType feedbackType
) {
    public TeamUpdateDto(TeamUpdateRequest request) {
        this(request.name(), request.startDate(), request.endDate(), request.feedbackType());
    }
}
