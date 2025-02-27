package com.feedhanjum.back_end.schedule.service.dto;

import com.feedhanjum.back_end.schedule.repository.dto.ScheduleProjectionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleNestedDto{
    @Schema(description = "일정 ID")
    private Long scheduleId;
    @Schema(description = "일정 이름")
    private String scheduleName;
    @Schema(description = "일정 시작 시간")
    private LocalDateTime startTime;
    @Schema(description = "일정 종료 시간")
    private LocalDateTime endTime;
    @Schema(description = "팀 ID")
    private Long teamId;
    @Schema(description = "팀 이름")
    private String teamName;
    @Schema(description = "리더 ID")
    private Long leaderId;
    @Schema(description = "일정 주인 ID")
    private Long ownerId;
    @Schema(description = "일정에 포함된 팀원 별 할 일 목록")
    private List<ScheduleMemberNestedDto> scheduleMemberNestedDtoList = new ArrayList<>();

    public ScheduleNestedDto(ScheduleProjectionDto scheduleProjectionDto) {
        scheduleId = scheduleProjectionDto.getScheduleId();
        scheduleName = scheduleProjectionDto.getScheduleName();
        startTime = scheduleProjectionDto.getStartTime();
        endTime = scheduleProjectionDto.getEndTime();
        teamId = scheduleProjectionDto.getTeamId();
        teamName = scheduleProjectionDto.getTeamName();
        leaderId = scheduleProjectionDto.getLeaderId();
        ownerId = scheduleProjectionDto.getOwnerId();
    }

    public void addScheduleMemberNestedDto(ScheduleMemberNestedDto scheduleMemberNestedDto){
        scheduleMemberNestedDtoList.add(scheduleMemberNestedDto);
    }
}
