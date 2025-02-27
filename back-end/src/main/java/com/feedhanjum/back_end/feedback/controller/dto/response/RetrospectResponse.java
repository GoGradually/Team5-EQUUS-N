package com.feedhanjum.back_end.feedback.controller.dto.response;

import com.feedhanjum.back_end.feedback.domain.Retrospect;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "회고 조회 응답")
public record RetrospectResponse(
        @Schema(description = "회고 제목")
        String title,
        @Schema(description = "팀 이름")
        String teamName,
        @Schema(description = "회고 내용")
        String content,
        @Schema(description = "작성일")
        LocalDateTime createdAt
) {
    public static RetrospectResponse from(Retrospect retrospect) {
        return RetrospectResponse.builder()
                .title(retrospect.getTitle())
                .content(retrospect.getContent())
                .createdAt(retrospect.getCreatedAt())
                .teamName(retrospect.getTeam().getName())
                .build();
    }
}
