package com.feedhanjum.back_end.feedback.controller.dto.request;

import com.feedhanjum.back_end.core.constraints.ByteLength;
import com.feedhanjum.back_end.feedback.domain.FrequentFeedbackRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FrequentFeedbackRequestForApiRequest(
        @Schema(description = "수시 피드백 요청의 수신자 ID")
        @NotNull
        Long receiverId,

        @Schema(description = "수시 피드백 요청을 보낼 소속 팀 ID")
        @NotNull
        Long teamId,

        @Schema(description = "피드백 요청 내용", example = "팀원들의 업무에 대한 피드백을 요청합니다.")
        @ByteLength(min = FrequentFeedbackRequest.MIN_REQUESTED_CONTENT_BYTE, max = FrequentFeedbackRequest.MAX_REQUESTED_CONTENT_BYTE, strip = true)
        String requestedContent
) {
}
