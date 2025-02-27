package com.feedhanjum.back_end.schedule.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "기간 내 조회 요청을 위한 클래스")
public record ScheduleBetweenDurationRequest (
    @Schema(description = "팀의 ID")
    Long teamId,
    @Schema(description = "조회 시작 날짜")
    @NotNull LocalDate startDay,
    @Schema(description = "조회 종료 날짜. 폐구간으로 계산됨")
    @NotNull LocalDate endDay
){
}