package com.feedhanjum.back_end.schedule.service.dto;

import com.feedhanjum.back_end.schedule.repository.dto.ScheduleProjectionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "스케줄 멤버 및 할 일 목록 데이터를 포함하는 DTO")
public class ScheduleMemberNestedDto{

    @Schema(description = "사용자의 ID")
    private Long memberId;
    
    @Schema(description = "사용자의 활동 이름")
    private String memberName;
    
    @Schema(description = "할 일 목록")
    private List<String> todoList = new ArrayList<>();

    public ScheduleMemberNestedDto(ScheduleProjectionDto scheduleProjectionDto){
        memberId = scheduleProjectionDto.getMemberId();
        memberName = scheduleProjectionDto.getMemberName();
    }

    public void addTodo(String todo){
        todoList.add(todo);
    }
}
