package com.feedhanjum.back_end.feedback.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RegularFeedbackRequestQueryRequest(
        @Schema(description = "정기 피드백 요청을 조회할 일정 ID")
        @NotNull
        Long scheduleId
) {
}
