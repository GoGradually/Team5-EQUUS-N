package com.feedhanjum.back_end.teamplanorchestration.domain;

import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.schedule.domain.Schedule;
import com.feedhanjum.back_end.schedule.exception.ScheduleAlreadyExistException;
import com.feedhanjum.back_end.schedule.exception.ScheduleIsAlreadyEndException;
import com.feedhanjum.back_end.schedule.service.dto.ScheduleRequestDto;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.team.service.dto.TeamUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TeamPlanOrchestrator {

    private final Clock clock;


    public void updateTeamInfo(TeamUpdateDto teamUpdateDto, LocalDateTime earliestStartTime, LocalDateTime latestEndTime, Member leader, Team team) {
        if (earliestStartTime != null && teamUpdateDto.startDate().atStartOfDay().isAfter(earliestStartTime)) {
            throw new IllegalArgumentException("팀 시작 날짜는 팀 내 존재하는 일정의 가장 빠른 시작 시점보다 빠를 수 없습니다.");
        }

        if (latestEndTime != null
                && teamUpdateDto.endDate() != null
                && teamUpdateDto.endDate().plusDays(1).atStartOfDay().isBefore(latestEndTime)) {
            throw new IllegalArgumentException("팀 종료 날짜는 팀 내 존재하는 일정의 가장 늦은 종료 시점보다 느릴 수 없습니다.");
        }

        team.updateInfo(leader, teamUpdateDto.teamName(), teamUpdateDto.startDate(), teamUpdateDto.endDate(), teamUpdateDto.feedbackType(), LocalDate.now(clock));
    }


    public Schedule createSchedule(ScheduleRequestDto requestDto, Schedule findSchedule, Member member, Team team) {
        // 겹치는 일정 찾아서 넣어주기
        validateScheduleDuplicate(findSchedule);
        validateScheduleTimeIntoTeamTime(requestDto, team);
        validateEndTimeIsAfterNow(requestDto.endTime());

        return new Schedule(
                requestDto.name(),
                requestDto.startTime(),
                requestDto.endTime(),
                team,
                member);
    }

    public void updateSchedule(ScheduleRequestDto requestDto, Schedule schedule, Schedule findSchedule, Member member, Team team) {
        validateIsEnded(schedule);
        changeName(schedule, requestDto, member, team);
        changeTime(schedule, requestDto, findSchedule, member, team);
    }

    private void validateIsEnded(Schedule schedule) {
        if (schedule.getEndTime().isBefore(LocalDateTime.now(clock))) {
            throw new ScheduleIsAlreadyEndException("해당 일정은 이미 종료되었습니다.");
        }
    }

    private void changeTime(Schedule schedule, ScheduleRequestDto requestDto, Schedule findSchedule, Member member, Team team) {
        if (schedule.isTimeDifferent(requestDto.startTime(), requestDto.endTime())) {
            validateOwnerOrLeader(schedule, member, team);
            if (schedule.isStartTimeDifferent(requestDto.startTime())) {
                // 겹치는 일정 찾아서 넣어주기
                validateScheduleDuplicate(findSchedule);
            }
            validateScheduleTimeIntoTeamTime(requestDto, team);
            validateEndTimeIsAfterNow(requestDto.endTime());
            schedule.setTime(requestDto.startTime(), requestDto.endTime());
        }
    }

    private void changeName(Schedule schedule, ScheduleRequestDto requestDto, Member member, Team team) {
        if (schedule.isNameDifferent(requestDto.name())) {
            validateOwnerOrLeader(schedule, member, team);
            schedule.changeName(requestDto.name());
        }
    }

    private void validateEndTimeIsAfterNow(LocalDateTime endTime) {
        if (LocalDateTime.now(clock).isAfter(endTime)) {
            throw new IllegalArgumentException("일정의 종료 시점은 현재보다 미래여야 합니다.");
        }
    }

    private void validateOwnerOrLeader(Schedule schedule, Member member, Team team) {
        if (!(schedule.getOwner().equals(member) || team.getLeader().equals(member))) {
            throw new SecurityException("일정을 생성한 사람, 혹은 팀장만 일정을 수정할 수 있습니다.");
        }
    }


    private void validateScheduleTimeIntoTeamTime(ScheduleRequestDto requestDto, Team team) {
        if (team.getStartDate().isAfter(requestDto.startTime().toLocalDate())) {
            throw new IllegalArgumentException("일정 시작 시간이 팀의 시작 시간 이후여야 합니다.");
        }
        if (team.getEndDate() != null &&
                team.getEndDate().isBefore(requestDto.endTime().toLocalDate())) {
            throw new IllegalArgumentException("일정 종료 시간이 팀의 종료 시간 이전이어야 합니다.");
        }
    }

    private void validateScheduleDuplicate(Schedule findSchedule) {
        if (findSchedule != null)
            throw new ScheduleAlreadyExistException("이미 같은 시작시간에 일정이 있습니다.");
    }
}
