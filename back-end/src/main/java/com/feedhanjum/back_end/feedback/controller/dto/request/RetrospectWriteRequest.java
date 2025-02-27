package com.feedhanjum.back_end.feedback.controller.dto.request;

import com.feedhanjum.back_end.core.constraints.ByteLength;
import com.feedhanjum.back_end.feedback.domain.Retrospect;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "회고 작성 요청")
public record RetrospectWriteRequest(
        @Schema(description = "작성자 ID")
        @NotNull
        Long writerId,

        @Schema(description = "팀 ID")
        @NotNull
        Long teamId,

        @Schema(description = "회고 제목")
        @NotBlank
        @Size(max = Retrospect.MAX_TITLE_LENGTH)
        String title,

        @Schema(description = "회고 내용")
        @ByteLength(max = Retrospect.MAX_CONTENT_BYTE, strip = true)
        String content
) {
}
