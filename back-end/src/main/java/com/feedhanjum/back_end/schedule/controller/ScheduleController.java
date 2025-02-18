package com.feedhanjum.back_end.schedule.controller;

import com.feedhanjum.back_end.auth.infra.Login;
import com.feedhanjum.back_end.schedule.controller.dto.ScheduleBetweenDurationRequest;
import com.feedhanjum.back_end.schedule.controller.dto.ScheduleRequest;
import com.feedhanjum.back_end.schedule.service.ScheduleService;
import com.feedhanjum.back_end.schedule.service.dto.ScheduleNestedDto;
import com.feedhanjum.back_end.schedule.service.dto.ScheduleRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "새 일정 만들기", description = "해당 팀의 새로운 일정을 만듭니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "일정 만들기 성공. 해당 팀의 모든 팀원을 생성된 일정에 등록함"),
            @ApiResponse(responseCode = "404", description = "팀 또는 사용자가 존재하지 않을 경우, 혹은 사용자가 해당 팀에 속해있지 않을 경우"),
            @ApiResponse(responseCode = "400", description = "주어진 양식을 지키지 못했을 경우"),
            @ApiResponse(responseCode = "409", description = "이미 주어진 시작시간에 일정이 있을 경우")

    })
    @PostMapping("/team/{teamId}/schedule/create")
    public ResponseEntity<Void> createSchedule(@Login Long memberId, @PathVariable Long teamId, @Valid @RequestBody ScheduleRequest request) {
        scheduleService.createSchedule(memberId, teamId, new ScheduleRequestDto(request));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "일정 수정하기", description = "특정 일정의 일정/할 일 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "일정/할 일 정보를 수정에 성공함."),
            @ApiResponse(responseCode = "403", description = "일정을 수정하려는 사용자가 팀장 혹은 일정의 주인이 아닌경우"),
            @ApiResponse(responseCode = "404", description = "일정, 팀, 혹은 일정과 사용자의 관계를 찾을 수 없는 경우")
    })
    @PostMapping("/team/{teamId}/schedule/{scheduleId}")
    public ResponseEntity<Void> updateSchedule(@Login Long memberId, 
                                               @PathVariable Long teamId, 
                                               @PathVariable Long scheduleId, 
                                               @Valid @RequestBody ScheduleRequest request) {
        scheduleService.updateSchedule(memberId, teamId, scheduleId, new ScheduleRequestDto(request));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "일정 삭제하기", description = "특정 일정을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "일정 삭제에 성공함."),
            @ApiResponse(responseCode = "403", description = "일정을 삭제하려는 사용자가 팀장 혹은 일정의 주인이 아닌 경우", content = @Content),
            @ApiResponse(responseCode = "404", description = "일정, 팀, 혹은 일정과 사용자의 관계를 찾을 수 없는 경우", content = @Content),
            @ApiResponse(responseCode = "410", description = "일정이 이미 종료된 경우")
    })
    @DeleteMapping("/team/{teamId}/schedule/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@Login Long memberId,
                                               @PathVariable Long teamId,
                                               @PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(memberId, teamId, scheduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "특정 일정 조회하기", description = "특정 팀의 특정 일정 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 조회를 성공함"),
            @ApiResponse(responseCode = "404", description = "일정을 조회할 권한이 없는 경우 - 일정 숨기기", content = @Content)
    })
    @GetMapping("/team/{teamId}/schedule/{scheduleId}")
    public ResponseEntity<ScheduleNestedDto> getSchedule(@Login Long memberId,
                                               @PathVariable Long teamId,
                                               @PathVariable Long scheduleId
                                               ) {
        ScheduleNestedDto schedule = scheduleService.getSchedule(memberId, teamId, scheduleId);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @Operation(summary = "기간 사이의 일정 조회하기", description = "특정 날짜와 날짜 사이에 존재하는 모든 일정을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기간 사이의 일정 조회에 성공함"),
            @ApiResponse(responseCode = "404", description = "해당 팀에 가입하지 않은 사용자인 경우", content = @Content),
            @ApiResponse(responseCode = "400", description = "입력 폼이 잘못된 경우", content = @Content)
    })
    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleNestedDto>> getScheduleBetweenDurations(
            @Login Long memberId,
            @Valid ScheduleBetweenDurationRequest scheduleBetweenDurationRequest
    ){
        List<ScheduleNestedDto> scheduleDurations = scheduleService.getScheduleDurations(
                memberId,
                scheduleBetweenDurationRequest.teamId(),
                scheduleBetweenDurationRequest.startDay(),
                scheduleBetweenDurationRequest.endDay()
        );
        return new ResponseEntity<>(scheduleDurations, HttpStatus.OK);
    }

    @Operation(summary = "팀 메인 화면 일정 조회", description = "특정 팀에 대해서, 메인 화면에 보여줘야 하는 일정을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메인화면 일정 조회에 성공함"),
            @ApiResponse(responseCode = "404", description = "해당 팀에 대한 일정 조회 권한이 없는 경우", content = @Content)
    })
    @GetMapping("/team/{teamId}/schedule")
    public ResponseEntity<ScheduleNestedDto> getNearestSchedule(
            @Login Long memberId,
            @PathVariable Long teamId
    ){
        ScheduleNestedDto nearestSchedule = scheduleService.getNearestSchedule(memberId, teamId);
        return new ResponseEntity<>(nearestSchedule, HttpStatus.OK);
    }
}
