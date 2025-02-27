package com.feedhanjum.back_end.feedback.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FrequentFeedbackRequestQueryRequest(
        @Schema(description = "수시 피드백 요청을 조회할 팀 ID")
        @NotNull
        Long teamId
) {
}
