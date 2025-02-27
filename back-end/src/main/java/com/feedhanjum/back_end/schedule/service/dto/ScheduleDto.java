package com.feedhanjum.back_end.schedule.service.dto;

import com.feedhanjum.back_end.schedule.domain.Schedule;

import java.time.LocalDateTime;

public record ScheduleDto (
        Long id,
        String name,
        LocalDateTime startTime,
        LocalDateTime endTime){
    public ScheduleDto(Schedule schedule){
        this(schedule.getId(), schedule.getName(), schedule.getStartTime(),schedule.getEndTime());
    }
}
