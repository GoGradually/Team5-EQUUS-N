package com.feedhanjum.back_end.schedule.service.dto;

import com.feedhanjum.back_end.schedule.domain.Schedule;
import com.feedhanjum.back_end.schedule.domain.ScheduleMember;
import lombok.Data;

import java.util.List;

@Data
public class ScheduleResponseDto {
    private final ScheduleDto schedule;
    private final List<TodoListDto> todoListDto;
    public ScheduleResponseDto(Schedule schedule, List<ScheduleMember> scheduleMember) {
        this.schedule = new ScheduleDto(schedule);
        this.todoListDto = scheduleMember.stream()
                .map(TodoListDto::new)
                .toList();
    }
}