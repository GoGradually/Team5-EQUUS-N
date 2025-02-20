package com.feedhanjum.back_end.team.controller.dto;

import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "팀 생성 요청")
public record TeamCreateRequest(
        @Schema(description = "팀 이름")
        @NotBlank @Size(min = 1, max = 10) String name,
        @Schema(description = "시작 날짜")
        @NotNull LocalDate startDate,
        @Schema(description = "종료 날짜")
        LocalDate endDate,
        @Schema(description = "피드백 유형")
        @NotNull FeedbackType feedbackType
) {
}
