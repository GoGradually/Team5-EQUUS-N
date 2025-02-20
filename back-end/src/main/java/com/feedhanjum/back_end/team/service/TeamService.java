package com.feedhanjum.back_end.team.service;

import com.feedhanjum.back_end.event.EventPublisher;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.schedule.repository.ScheduleQueryRepository;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.team.domain.TeamJoinToken;
import com.feedhanjum.back_end.team.event.TeamLeaderChangedEvent;
import com.feedhanjum.back_end.team.event.TeamMemberJoinEvent;
import com.feedhanjum.back_end.team.event.TeamMemberLeftEvent;
import com.feedhanjum.back_end.team.exception.TeamLeaderMustExistException;
import com.feedhanjum.back_end.team.exception.TeamMembershipNotFoundException;
import com.feedhanjum.back_end.team.repository.TeamJoinTokenRepository;
import com.feedhanjum.back_end.team.repository.TeamQueryRepository;
import com.feedhanjum.back_end.team.repository.TeamRepository;
import com.feedhanjum.back_end.team.service.dto.TeamCreateDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TeamQueryRepository teamQueryRepository;
    private final EventPublisher eventPublisher;
    private final TeamJoinTokenRepository teamJoinTokenRepository;
    private final ScheduleQueryRepository scheduleQueryRepository;
    private final Clock clock;

    /**
     * @throws IllegalArgumentException 프로젝트 기간의 시작일이 종료일보다 앞서지 않을 경우
     * @throws EntityNotFoundException  팀 생성 요청한 리더가 존재하지 않을 경우
     */
    @Transactional
    public Team createTeam(Long leaderId, TeamCreateDto teamCreateDto) {
        Member leader = memberRepository.findById(leaderId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        Team team = new Team(teamCreateDto.teamName(), leader, teamCreateDto.startDate(), teamCreateDto.endDate(), teamCreateDto.feedbackType(), LocalDate.now(clock));
        teamRepository.save(team);
        return team;
    }

    @Transactional(readOnly = true)
    public Team getTeam(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("해당 팀을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Team> getMyTeams(Long userId) {
        return teamQueryRepository.findTeamByMemberId(userId);
    }

    /**
     * 팀장이 팀원을 제거한다.
     *
     * @throws EntityNotFoundException      해당 팀 또는 팀원 정보가 없을 경우
     * @throws SecurityException            요청자가 팀장이 아닐 경우
     * @throws TeamLeaderMustExistException 팀장(자기 자신)을 제거하려 할 경우
     */
    @Transactional
    public void removeTeamMember(Long leaderId, Long teamId, Long memberIdToRemove) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));

        Member leader = memberRepository.findById(leaderId)
                .orElseThrow(() -> new EntityNotFoundException("팀장을 찾을 수 없습니다"));
        Member removeTarget = memberRepository.findById(memberIdToRemove)
                .orElseThrow(() -> new EntityNotFoundException("팀원을 찾을 수 없습니다"));

        team.expel(leader, removeTarget);

        eventPublisher.publishEvent(new TeamMemberLeftEvent(teamId, memberIdToRemove));
    }

    /**
     * @throws EntityNotFoundException         해당 팀 또는 회원이 존재하지 않을 경우
     * @throws SecurityException               현재 사용자가 팀장이 아닐 경우
     * @throws TeamMembershipNotFoundException 새 팀장이 팀의 구성원이 아닐 경우
     */
    @Transactional
    public void delegateTeamLeader(Long currentLeaderId, Long teamId, Long newLeaderId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));
        Member currentLeader = memberRepository.findById(currentLeaderId).orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다"));
        Member newLeader = memberRepository.findById(newLeaderId).orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다"));

        team.changeLeader(currentLeader, newLeader);
        eventPublisher.publishEvent(new TeamLeaderChangedEvent(teamId, newLeaderId));
    }



    /**
     * @throws EntityNotFoundException      팀 가입 정보가 없을 경우
     * @throws TeamLeaderMustExistException 팀장은 탈퇴할 수 없으므로 발생
     */
    @Transactional
    public void leaveTeam(Long memberId, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다"));

        team.leave(member);
        if (team.memberCount() == 0) {
            deleteTeam(team);
        }
        eventPublisher.publishEvent(new TeamMemberLeftEvent(teamId, memberId));
    }

    @Transactional
    public TeamJoinToken createJoinToken(Long memberId, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));
        Member member = memberRepository
                .findById(memberId).orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다"));
        TeamJoinToken joinToken = team.createJoinToken(member, LocalDateTime.now(clock));
        teamJoinTokenRepository.save(joinToken);
        return joinToken;
    }

    @Transactional(readOnly = true)
    public Team getTeamByJoinToken(String token) {
        TeamJoinToken teamJoinToken = teamJoinTokenRepository.findById(token)
                .orElseThrow(() -> new EntityNotFoundException("토큰이 유효하지 않습니다."));
        return teamJoinToken.getTeamInfo();
    }

    @Transactional
    public void joinTeam(Long memberId, String token) {
        TeamJoinToken teamJoinToken = teamJoinTokenRepository.findById(token)
                .orElseThrow(() -> new EntityNotFoundException("토큰이 유효하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다"));
        teamJoinToken.joinTeam(member);
        eventPublisher.publishEvent(new TeamMemberJoinEvent(memberId, teamJoinToken.getTeamInfo().getId()));
    }


    private void deleteTeam(Team team) {
        teamRepository.delete(team);
    }
}
