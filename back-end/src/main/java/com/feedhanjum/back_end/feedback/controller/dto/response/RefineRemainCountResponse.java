package com.feedhanjum.back_end.feedback.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "피드백 다듬기 호출 가능 횟수 응답")
public record RefineRemainCountResponse(
        @Schema(description = "남은 호출 가능 횟수")
        @Max(3)
        @Min(0)
        int remainCount
) {
}
