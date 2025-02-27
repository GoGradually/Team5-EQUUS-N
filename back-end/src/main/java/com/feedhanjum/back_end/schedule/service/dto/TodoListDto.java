package com.feedhanjum.back_end.schedule.service.dto;

import com.feedhanjum.back_end.schedule.domain.ScheduleMember;
import com.feedhanjum.back_end.schedule.domain.Todo;

import java.util.List;
import java.util.Objects;

public record TodoListDto (Long memberId, String name, List<Todo> todos){
    public TodoListDto(ScheduleMember scheduleMember){
        this(Objects.requireNonNull(scheduleMember, "scheduleMember must not be null").getMember().getId(),
                scheduleMember.getMember().getName(),
                List.copyOf(scheduleMember.getTodos()));
    }
}
