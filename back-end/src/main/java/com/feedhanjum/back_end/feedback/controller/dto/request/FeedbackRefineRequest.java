package com.feedhanjum.back_end.feedback.controller.dto.request;

import com.feedhanjum.back_end.core.constraints.ByteLength;
import com.feedhanjum.back_end.feedback.domain.Feedback;
import com.feedhanjum.back_end.feedback.domain.ObjectiveFeedback;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "피드백 다듬기 요청")
public record FeedbackRefineRequest(
        @Schema(description = "피드백 수신자 ID")
        @NotNull
        Long receiverId,

        @Schema(description = "선택한 객관식 피드백 목록")
        @Size(min = Feedback.MIN_OBJECTIVE_FEEDBACK_SIZE, max = Feedback.MAX_OBJECTIVE_FEEDBACK_SIZE)
        List<ObjectiveFeedback> objectiveFeedbacks,

        @Schema(description = "다듬을 주관식 피드백")
        @NotBlank
        @ByteLength(min = Feedback.MIN_SUBJECTIVE_FEEDBACK_BYTE, max = Feedback.MAX_SUBJECTIVE_FEEDBACK_BYTE, strip = true)
        String subjectiveFeedback
) {
}
