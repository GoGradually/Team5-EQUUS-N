package com.feedhanjum.back_end.schedule.service;

import com.feedhanjum.back_end.core.domain.JobRecord;
import com.feedhanjum.back_end.core.event.EventPublisher;
import com.feedhanjum.back_end.core.repository.JobRecordRepository;
import com.feedhanjum.back_end.feedback.repository.RegularFeedbackRequestQueryRepository;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.repository.MemberQueryRepository;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.schedule.domain.Schedule;
import com.feedhanjum.back_end.schedule.domain.ScheduleMember;
import com.feedhanjum.back_end.schedule.event.ScheduleEndedEvent;
import com.feedhanjum.back_end.schedule.exception.ScheduleIsAlreadyEndException;
import com.feedhanjum.back_end.schedule.repository.ScheduleMemberRepository;
import com.feedhanjum.back_end.schedule.repository.ScheduleQueryRepository;
import com.feedhanjum.back_end.schedule.repository.ScheduleRepository;
import com.feedhanjum.back_end.schedule.repository.dto.ScheduleProjectionDto;
import com.feedhanjum.back_end.schedule.service.dto.ScheduleMemberNestedDto;
import com.feedhanjum.back_end.schedule.service.dto.ScheduleNestedDto;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.team.exception.TeamMembershipNotFoundException;
import com.feedhanjum.back_end.team.repository.TeamMemberRepository;
import com.feedhanjum.back_end.team.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final Clock clock;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final MemberRepository memberRepository;
    private final ScheduleQueryRepository scheduleQueryRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final JobRecordRepository jobRecordRepository;
    private final EventPublisher eventPublisher;
    private final RegularFeedbackRequestQueryRepository regularFeedbackRequestQueryRepository;


    @Transactional(readOnly = true)
    public ScheduleNestedDto getSchedule(Long memberId, Long teamId, Long scheduleId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));
        memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
        teamMemberRepository.findByMemberIdAndTeamId(memberId, teamId).orElseThrow(() -> new TeamMembershipNotFoundException("해당 팀에 속해있지 않습니다."));

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("해당 일정을 찾을 수 없습니다."));
        if (!schedule.getTeam().equals(team)) {
            throw new TeamMembershipNotFoundException("해당 팀에 속해있지 않습니다.");
        }
        List<ScheduleProjectionDto> scheduleTodoList = scheduleQueryRepository.findScheduleTodoList(scheduleId, null);

        return getScheduleNestedDtos(scheduleTodoList).stream().findFirst().orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ScheduleNestedDto> getScheduleDurations(Long memberId, Long teamId, LocalDate startDay, LocalDate endDay) {
        memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
        if (teamId != null) {
            memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
            teamMemberRepository.findByMemberIdAndTeamId(memberId, teamId).orElseThrow(() -> new TeamMembershipNotFoundException("해당 팀에 속해있지 않습니다."));
        }
        if (startDay.isAfter(endDay)) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜 이전이어야 합니다.");
        }
        LocalDateTime startTime = startDay.atStartOfDay();
        LocalDateTime endTime = endDay.atTime(LocalTime.MAX);
        List<ScheduleProjectionDto> schedulesByTeamIdAndDuration = scheduleQueryRepository.findSchedulesByTeamIdAndDuration(memberId, teamId, startTime, endTime);

        return getScheduleNestedDtos(schedulesByTeamIdAndDuration);
    }

    @Transactional(readOnly = true)
    public ScheduleNestedDto getNearestSchedule(Long memberId, Long teamId) {
        validateTeamMember(memberId, teamId);

        LocalDateTime now = LocalDateTime.now(clock);

        ScheduleNestedDto nextSchedule = getScheduleNestedDtos(scheduleQueryRepository.findScheduleByClosestNextStartTime(teamId, now))
                .stream().findFirst().orElse(null);
        ScheduleNestedDto previousSchedule = getScheduleNestedDtos(scheduleQueryRepository.findScheduleByClosestPreviousEndTime(teamId, now))
                .stream().findFirst().orElse(null);

        // 가까운 미래 일정이 1일 이내라면 바로 반환
        if (nextSchedule != null && nextSchedule.getStartTime().isBefore(now.plusDays(1)))
            return nextSchedule;

        // 지난 일정이 1일 이내이고, 처리하지 않은 정기 피드백 요청이 존재한다면 이전 일정 반환
        if (previousSchedule != null && previousSchedule.getEndTime().isAfter(now.minusDays(1))
                && regularFeedbackRequestQueryRepository.getRegularFeedbackRequestCount(memberId, previousSchedule.getScheduleId()) > 0) {
            return previousSchedule;
        }

        // 위 조건에 부합하지 않으면 nextSchedule 반환
        return nextSchedule;
    }

    @Transactional
    public void endSchedules() {
        LocalDateTime now = truncateToNearestTenMinutes(LocalDateTime.now(clock));
        JobRecord jobRecord = jobRecordRepository.findById(JobRecord.JobName.SCHEDULE)
                .orElseGet(() -> new JobRecord(JobRecord.JobName.SCHEDULE));

        List<Schedule> schedules = scheduleRepository.findByEndTimeBetween(jobRecord.getPreviousFinishTime().plusSeconds(1), now);
        for (Schedule schedule : schedules) {
            eventPublisher.publishEvent(new ScheduleEndedEvent(schedule.getId()));
        }
        jobRecord.updatePreviousFinishTime(now);
        jobRecordRepository.save(jobRecord);
    }

    /**
     * Retrieves the earliest start time among all schedules for a given team.
     *
     * @param teamId The ID of the team
     * @return The earliest schedule start time, or null if no schedules exist
     * @throws IllegalArgumentException if teamId is null
     */
    @Transactional(readOnly = true)
    public LocalDateTime getEarliestScheduleStartTime(Long teamId) {
        if (teamId == null) {
            throw new IllegalArgumentException("Team ID cannot be null");
        }
        return scheduleQueryRepository.findEarliestStartTimeByTeamId(teamId).orElse(null);
    }

    /**
     * Retrieves the latest end time among all schedules for a given team.
     *
     * @param teamId The ID of the team
     * @return The latest schedule end time, or null if no schedules exist
     * @throws IllegalArgumentException if teamId is null
     */
    @Transactional(readOnly = true)
    public LocalDateTime getLatestScheduleEndTime(Long teamId) {
        if (teamId == null) {
            throw new IllegalArgumentException("Team ID cannot be null");
        }
        return scheduleQueryRepository.findLatestEndTimeByTeamId(teamId).orElse(null);
    }

    @Transactional
    public void addNewScheduleMembership(Long memberId, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID " + teamId + " does not exist"));
        List<Schedule> relatedSchedule = scheduleRepository.findAllByTeam_IdAndEndTimeGreaterThanEqual(teamId, LocalDateTime.now(clock));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member with ID " + memberId + " does not exist"));
        if (relatedSchedule.isEmpty()) {
            return;  // Early return if no schedules need updating
        }
        List<ScheduleMember> scheduleMembers = relatedSchedule.stream()
                .filter(schedule -> scheduleMemberRepository.findByMemberIdAndScheduleId(member.getId(), schedule.getId()).isEmpty())
                .map(schedule -> new ScheduleMember(schedule, member))
                .toList();
        scheduleMemberRepository.saveAll(scheduleMembers);
    }

    @Transactional
    public void removeRemainScheduleMembership(Long memberId, Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new EntityNotFoundException("Team with ID " + teamId + " does not exist");
        }
        if (!memberRepository.existsById(memberId)) {
            throw new EntityNotFoundException("Member with ID " + memberId + " does not exist");
        }
        scheduleMemberRepository.deleteScheduleMembersByMemberIdAndTeamIdAfterNow(memberId, teamId, LocalDateTime.now(clock));
    }

    @Transactional
    public void deleteSchedule(Long memberId, Long teamId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("해당 일정을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("해당 팀을 찾을 수 없습니다."));
        if (!schedule.getTeam().equals(team)) {
            throw new SecurityException("해당 일정을 삭제할 권한이 없습니다.");
        }
        if (!schedule.getOwner().equals(member) && !team.getLeader().equals(member)) {
            throw new SecurityException("해당 일정을 삭제할 권한이 없습니다.");
        }
        if (schedule.isEnd()) throw new ScheduleIsAlreadyEndException("일정이 이미 종료되었습니다.");
        scheduleRepository.delete(schedule);
    }

    private LocalDateTime truncateToNearestTenMinutes(LocalDateTime dateTime) {
        int minute = dateTime.getMinute();
        int truncatedMinute = (minute / 10) * 10;
        return dateTime.withMinute(truncatedMinute).withSecond(0).withNano(0);
    }

    private List<ScheduleNestedDto> getScheduleNestedDtos(List<ScheduleProjectionDto> schedules) {
        Map<Long, ScheduleNestedDto> scheduleNestedDtoMap = new HashMap<>();
        Map<Long, ScheduleMemberNestedDto> scheduleMemberNestedDtoMap = new HashMap<>();

        for (ScheduleProjectionDto dto : schedules) {
            Long scheduleId = dto.getScheduleId();
            Long scheduleMemberId = dto.getScheduleMemberId();

            ScheduleNestedDto scheduleNestedDto = scheduleNestedDtoMap.computeIfAbsent(scheduleId, id -> new ScheduleNestedDto(dto));
            ScheduleMemberNestedDto scheduleMemberNestedDto;
            if (scheduleMemberNestedDtoMap.get(scheduleMemberId) == null) {
                scheduleMemberNestedDto = new ScheduleMemberNestedDto(dto);
                scheduleMemberNestedDtoMap.put(scheduleMemberId, scheduleMemberNestedDto);
                scheduleNestedDto.addScheduleMemberNestedDto(scheduleMemberNestedDto);
            }
            scheduleMemberNestedDto = scheduleMemberNestedDtoMap.get(scheduleMemberId);

            if (dto.getTodo() != null) scheduleMemberNestedDto.addTodo(dto.getTodo());
        }
        return new ArrayList<>(scheduleNestedDtoMap.values());
    }

    private void validateTeamMember(Long memberId, Long teamId) {
        teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));
        memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
        teamMemberRepository.findByMemberIdAndTeamId(memberId, teamId).orElseThrow(() -> new TeamMembershipNotFoundException("해당 팀에 속해있지 않습니다."));
    }
}
