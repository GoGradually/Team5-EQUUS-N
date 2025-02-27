package com.feedhanjum.back_end.teamplanorchestration.service;

import com.feedhanjum.back_end.core.event.EventPublisher;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.repository.MemberQueryRepository;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.schedule.domain.Schedule;
import com.feedhanjum.back_end.schedule.domain.ScheduleMember;
import com.feedhanjum.back_end.schedule.event.ScheduleCreatedEvent;
import com.feedhanjum.back_end.schedule.exception.ScheduleAlreadyExistException;
import com.feedhanjum.back_end.schedule.exception.ScheduleMembershipNotFoundException;
import com.feedhanjum.back_end.schedule.repository.ScheduleMemberRepository;
import com.feedhanjum.back_end.schedule.repository.ScheduleQueryRepository;
import com.feedhanjum.back_end.schedule.repository.ScheduleRepository;
import com.feedhanjum.back_end.schedule.service.dto.ScheduleRequestDto;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.team.exception.TeamMembershipNotFoundException;
import com.feedhanjum.back_end.team.repository.TeamMemberRepository;
import com.feedhanjum.back_end.team.repository.TeamRepository;
import com.feedhanjum.back_end.team.service.dto.TeamUpdateDto;
import com.feedhanjum.back_end.teamplanorchestration.domain.TeamPlanOrchestrator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TeamPlanOrchestrationService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final ScheduleQueryRepository scheduleQueryRepository;
    private final EventPublisher eventPublisher;
    private final TeamPlanOrchestrator teamPlanOrchestrator;

    /**
     * @throws IllegalArgumentException 시작 시간이 종료 시간보다 앞서지 않을 경우
     * @throws EntityNotFoundException  팀이 존재하지 않는 경우
     * @throws SecurityException        요청자가 팀장이 아닐 경우
     */
    @Transactional
    public Team updateTeamInfo(Long leaderId, Long teamId, TeamUpdateDto teamUpdateDto) {
        Team team = teamRepository.findByIdForUpdateExclusiveLock(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));
        Member leader = memberRepository.findById(leaderId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다"));

        LocalDateTime earliestStartTime = scheduleQueryRepository.findEarliestStartTimeByTeamId(teamId).orElse(null);
        LocalDateTime latestEndTime = scheduleQueryRepository.findLatestEndTimeByTeamId(teamId).orElse(null);

        teamPlanOrchestrator.updateTeamInfo(teamUpdateDto, earliestStartTime, latestEndTime, leader, team);
        return team;
    }


    /**
     * 일정을 생성하는 메소드
     *
     * @throws EntityNotFoundException         사용자 혹은 팀이 존재하지 않는 경우
     * @throws TeamMembershipNotFoundException 해당 사용자가 팀에 가입한 상태가 아닌경우
     * @throws ScheduleAlreadyExistException   해당 일정의 시작 시간에 일정이 존재하는 경우
     * @throws RuntimeException                내부 서버 오류: 방금 조회한 사용자 ID가 사라짐
     */
    @Transactional
    public void createSchedule(Long memberId, Long teamId, ScheduleRequestDto requestDto) {
        Team team = teamRepository.findByIdForUpdateSharedLock(teamId).orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
        teamMemberRepository.findByMemberIdAndTeamId(memberId, teamId).orElseThrow(() -> new TeamMembershipNotFoundException("해당 팀에 존재하는 사람만 일정을 생성할 수 있습니다."));
        Schedule findSchedule = scheduleRepository.findByTeamIdAndStartTime(teamId, requestDto.startTime()).orElse(null);

        Schedule schedule = scheduleRepository.save(teamPlanOrchestrator.createSchedule(requestDto, findSchedule, member, team));

        // 이벤트로 빼서, 일정에서 등록하도록 하기
        memberQueryRepository.findMembersByTeamId(teamId).forEach(m -> scheduleMemberRepository.save(new ScheduleMember(schedule, m)));
        ScheduleMember scheduleMember = scheduleMemberRepository.findByMemberIdAndScheduleId(memberId, schedule.getId()).orElseThrow(() -> new RuntimeException("내부 서버 에러: 방금 조회한 사용자 ID가 사라짐"));
        eventPublisher.publishEvent(new ScheduleCreatedEvent(schedule.getId()));

        // 이벤트로 빼서, 일정에서 해결하도록 하기
        scheduleMember.setTodos(requestDto.todos());
    }


    /**
     * @throws SecurityException                   일정 내용을 수정하려는 사람이 팀장, 일정의 주인이 아닌 경우
     * @throws EntityNotFoundException             팀, 사용자 또는 일정을 찾을 수 없는 경우
     * @throws ScheduleMembershipNotFoundException 회원이 해당 일정과 연관이 없을 경우
     * @throws IllegalArgumentException            일정의 시간 설정이 잘못된 경우
     */

    @Transactional
    public void updateSchedule(Long memberId, Long teamId, Long scheduleId, ScheduleRequestDto requestDto) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("해당 일정을 찾을 수 없습니다."));
        ScheduleMember scheduleMember = scheduleMemberRepository.findByMemberIdAndScheduleId(memberId, scheduleId)
                .orElseThrow(() -> new ScheduleMembershipNotFoundException("해당 일정을 찾을 수 없습니다."));
        Schedule findSchedule = scheduleRepository.findByTeamIdAndStartTime(teamId, requestDto.startTime()).orElse(null);

        teamPlanOrchestrator.updateSchedule(requestDto, schedule, findSchedule, member, team);

        // 이벤트로 빼서, 일정에서 해결하도록 하기
        scheduleMember.setTodos(requestDto.todos());
    }




}
