package com.feedhanjum.back_end.schedule.controller.dto;

import com.feedhanjum.back_end.schedule.domain.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record ScheduleRequest(
        @Schema(description = "일정 이름") @NotBlank @Size(min = 1, max = 20) String name,
        @Schema(description = "시작 시간") @NotNull LocalDateTime startTime,
        @Schema(description = "종료 시간") @NotNull LocalDateTime endTime,
        @Valid @Schema(description = "일정에 포함된 로그인한 사용자의 할일 목록") List<Todo> todos
) {
}
