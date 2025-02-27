package com.feedhanjum.back_end.feedback.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public record ReceivedFeedbacksQueryRequest(
        @Schema(description = "받은 피드백을 조회할 사용자 ID")
        @NotNull
        Long receiverId,

        @Schema(description = "팀 ID")
        @Nullable
        Long teamId,

        @Schema(description = "도움이 되는 피드백만 필터링할지 여부", defaultValue = "false")
        @Nullable
        Boolean filterHelpful,

        @Schema(description = "페이지 번호", defaultValue = "0", minimum = "0")
        @Nullable
        Integer page,

        @Schema(description = "정렬 방식", defaultValue = "DESC")
        @Nullable
        Sort.Direction sortOrder
) {
    public ReceivedFeedbacksQueryRequest(
            Long receiverId,
            Long teamId,
            Boolean filterHelpful,
            Integer page,
            Sort.Direction sortOrder) {
        this.receiverId = receiverId;
        this.teamId = teamId;
        this.filterHelpful = Objects.requireNonNullElse(filterHelpful, false);
        this.page = Objects.requireNonNullElse(page, 0);
        this.sortOrder = Objects.requireNonNullElse(sortOrder, Sort.Direction.DESC);


    }
}
