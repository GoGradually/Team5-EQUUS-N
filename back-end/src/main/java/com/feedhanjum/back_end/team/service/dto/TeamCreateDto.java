package com.feedhanjum.back_end.team.service.dto;

import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.team.controller.dto.TeamCreateRequest;

import java.time.LocalDate;

public record TeamCreateDto(
        String teamName,
        LocalDate startDate,
        LocalDate endDate,
        FeedbackType feedbackType
) {
    public TeamCreateDto(TeamCreateRequest request) {
        this(request.name(), request.startDate(), request.endDate(), request.feedbackType());
    }
}
