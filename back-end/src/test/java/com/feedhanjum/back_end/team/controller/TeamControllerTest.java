package com.feedhanjum.back_end.team.controller;

import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.member.controller.dto.MemberDto;
import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.service.MemberService;
import com.feedhanjum.back_end.schedule.service.ScheduleService;
import com.feedhanjum.back_end.team.controller.dto.TeamCreateRequest;
import com.feedhanjum.back_end.team.controller.dto.TeamDetailResponse;
import com.feedhanjum.back_end.team.controller.dto.TeamResponse;
import com.feedhanjum.back_end.team.controller.dto.TeamUpdateRequest;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.team.service.TeamService;
import com.feedhanjum.back_end.team.service.dto.TeamCreateDto;
import com.feedhanjum.back_end.team.service.dto.TeamUpdateDto;
import com.feedhanjum.back_end.teamplanorchestration.service.TeamPlanOrchestrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @Mock
    private MemberService memberService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private TeamPlanOrchestrationService teamPlanOrchestrationService;

    @InjectMocks
    private TeamController teamController;


    @Test
    @DisplayName("팀 생성 컨트롤러 테스트")
    void createTeam_팀생성_컨트롤러() {
        //given
        Long memberId = 1L;
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(10);
        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        TeamCreateRequest request = new TeamCreateRequest("haha", startDate,
                endDate, FeedbackType.ANONYMOUS);
        TeamCreateDto teamCreateDto = new TeamCreateDto(request);
        TeamResponse teamResponse = new TeamResponse(null, "haha", startDate,
                endDate, FeedbackType.ANONYMOUS, new MemberDto(new Member("haha", "haha@hoho", null, feedbackPreferences)));
        when(teamService.createTeam(memberId, teamCreateDto))
                .thenReturn(new Team("haha", new Member("haha", "haha@hoho", null, feedbackPreferences),
                        request.startDate(), request.endDate(), request.feedbackType(), LocalDate.now()));

        //when
        ResponseEntity<TeamResponse> response = teamController.createTeam(memberId, request);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(teamResponse);
    }

    @Test
    @DisplayName("내 팀 조회 컨트롤러 테스트")
    void getMyTeams_팀조회_컨트롤러() {
        //given
        Long memberId = 1L;
        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        Team team = new Team("haha", new Member("haha", "haha@hoho", null, feedbackPreferences), LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10), FeedbackType.ANONYMOUS, LocalDate.now());
        TeamResponse teamResponse = new TeamResponse(team);
        when(teamService.getMyTeams(memberId)).thenReturn(List.of(team));

        //when
        ResponseEntity<List<TeamResponse>> response = teamController.getMyTeams(memberId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0)).isEqualTo(teamResponse);
    }

    @Test
    @DisplayName("팀 상세 조회시 팀 정보와 멤버 리스트를 정상적으로 반환하는지 테스트")
    void getTeamDetail_팀상세조회() {
        // given
        Long teamId = 1L;
        Long memberId = 2L;
        LocalDate now = LocalDate.now();

        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        Member leader = new Member("haha", "haha", null, feedbackPreferences);
        Team dummyTeam = new Team("haha", leader, now, now.plusDays(1), FeedbackType.IDENTIFIED, LocalDate.now());

        Member dummyMember = new Member("hoho", "huhu", null, feedbackPreferences);
        List<Member> memberList = List.of(dummyMember);

        when(teamService.getTeam(teamId)).thenReturn(dummyTeam);
        when(memberService.getMembersByTeam(memberId, teamId)).thenReturn(memberList);

        // when
        ResponseEntity<TeamDetailResponse> responseEntity = teamController.getTeamDetail(memberId, teamId);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        TeamDetailResponse teamDetailResponse = responseEntity.getBody();
        assertThat(teamDetailResponse).isNotNull();

        TeamResponse teamResponse = teamDetailResponse.getTeamResponse();
        assertThat(teamResponse).isNotNull();
        assertThat(teamResponse.name()).isEqualTo(dummyTeam.getName());
        assertThat(teamResponse.startDate()).isEqualTo(dummyTeam.getStartDate());
        assertThat(teamResponse.endDate()).isEqualTo(dummyTeam.getEndDate());
        assertThat(teamResponse.feedbackType()).isEqualTo(dummyTeam.getFeedbackType());

        assertThat(teamResponse.leader().name()).isEqualTo(dummyTeam.getLeader().getName());
        assertThat(teamResponse.leader().email()).isEqualTo(dummyTeam.getLeader().getEmail());

        List<MemberDto> members = teamDetailResponse.getMembers();
        assertThat(members).hasSize(memberList.size());
        MemberDto memberDto = members.get(0);
        assertThat(memberDto.name()).isEqualTo(dummyMember.getName());
        assertThat(memberDto.email()).isEqualTo(dummyMember.getEmail());
    }

    @Test
    @DisplayName("팀 멤버 조회시 팀 멤버 리스트를 정상적으로 반환하는지 테스트")
    void getTeamMembers_팀멤버조회() {
        // given
        Long teamId = 3L;
        Long memberId = 4L;

        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        Member dummyMember = new Member("hehe", "hoho", null, feedbackPreferences);
        List<Member> memberList = List.of(dummyMember);

        when(memberService.getMembersByTeam(memberId, teamId)).thenReturn(memberList);

        // when
        ResponseEntity<List<MemberDto>> responseEntity = teamController.getTeamMembers(memberId, teamId);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<MemberDto> memberDtos = responseEntity.getBody();
        assertThat(memberDtos).isNotNull();
        assertThat(memberDtos).hasSize(memberList.size());
        MemberDto memberDto = memberDtos.get(0);
        assertThat(memberDto.name()).isEqualTo(dummyMember.getName());
        assertThat(memberDto.email()).isEqualTo(dummyMember.getEmail());
    }

    @Test
    @DisplayName("팀 멤버 제거가 정상적으로 호출되는지 테스트")
    void deleteMemberFromTeam_팀원제거() {
        //given
        Long teamId = 1L;
        Long memberId = 2L;
        Long removeMemberId = 3L;

        //when
        teamController.deleteMemberFromTeam(memberId, teamId, removeMemberId);

        //then
        verify(teamService).removeTeamMember(memberId, teamId, removeMemberId);
    }

    @Test
    @DisplayName("컨트롤러 팀 리더 위임 정상 동작")
    void delegateTeamLeader_정상() {
        // given
        Long memberId = 1L;
        Long teamId = 1L;
        Long newLeaderId = 2L;

        // when
        ResponseEntity<Void> response = teamController.delegateTeamLeader(memberId, teamId, newLeaderId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("팀 탈퇴 API 호출")
    void leaveTeam_Success() throws Exception {
        // given
        Long teamId = 1L;
        Long memberId = 100L;

        // when
        teamController.leaveTeam(memberId, teamId);

        // then
        verify(teamService).leaveTeam(memberId, teamId);
    }

    @Test
    @DisplayName("팀 정보 변경 API 호출")
    void updateTeamInfo(){
        // given
        Long teamId = 1L;
        Long memberId = 100L;
        TeamUpdateRequest teamUpdateRequest = new TeamUpdateRequest("hehe", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), FeedbackType.IDENTIFIED);

        Team team = new Team("haha", mock(Member.class), LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), FeedbackType.ANONYMOUS, LocalDate.now());
        when(teamPlanOrchestrationService.updateTeamInfo(memberId, teamId, new TeamUpdateDto(teamUpdateRequest))).thenReturn(team);

        // when
        teamController.updateTeamInfo(memberId, teamId, teamUpdateRequest);

        // then
        verify(teamPlanOrchestrationService).updateTeamInfo(memberId, teamId, new TeamUpdateDto(teamUpdateRequest));
    }
}