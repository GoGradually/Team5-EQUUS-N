package com.feedhanjum.back_end.feedback.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Schema(description = "회고 조회 요청")
public record RetrospectsQueryRequest(
        @Schema(description = "회고를 작성한 사용자 ID")
        @NotNull
        Long writerId,

        @Schema(description = "필터링할 팀 ID")
        @Nullable
        Long teamId,

        @Schema(description = "페이지 번호", defaultValue = "0", minimum = "0")
        @Nullable
        Integer page,

        @Schema(description = "정렬 방식", defaultValue = "DESC")
        @Nullable
        Sort.Direction sortOrder
) {
    public RetrospectsQueryRequest(
            Long writerId,
            Long teamId,
            Integer page,
            Sort.Direction sortOrder) {
        this.writerId = writerId;
        this.teamId = teamId;
        this.page = Objects.requireNonNullElse(page, 0);
        this.sortOrder = Objects.requireNonNullElse(sortOrder, Sort.Direction.DESC);


    }
}
