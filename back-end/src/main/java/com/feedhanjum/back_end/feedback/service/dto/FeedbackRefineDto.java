package com.feedhanjum.back_end.feedback.service.dto;

import com.feedhanjum.back_end.core.constraints.ByteLength;
import com.feedhanjum.back_end.feedback.domain.Feedback;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "피드백 다듬기 응답")
public record FeedbackRefineDto(
        @Schema(description = "다듬어진 주관식 피드백")
        @NotBlank
        @ByteLength(min = Feedback.MIN_SUBJECTIVE_FEEDBACK_BYTE, max = Feedback.MAX_SUBJECTIVE_FEEDBACK_BYTE, strip = true)
        String subjectiveFeedback,

        @Schema(description = "남은 호출 가능 횟수")
        Long remainCount
) {
}
