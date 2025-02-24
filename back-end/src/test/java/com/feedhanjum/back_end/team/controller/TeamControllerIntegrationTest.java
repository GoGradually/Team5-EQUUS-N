package com.feedhanjum.back_end.team.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedhanjum.back_end.core.event.EventPublisher;
import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.member.controller.dto.MemberResponse;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.team.controller.dto.*;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.team.domain.TeamJoinToken;
import com.feedhanjum.back_end.team.domain.TeamMember;
import com.feedhanjum.back_end.team.event.TeamMemberLeftEvent;
import com.feedhanjum.back_end.team.repository.TeamJoinTokenRepository;
import com.feedhanjum.back_end.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.feedhanjum.back_end.test.util.DomainTestUtils.createMemberWithoutId;
import static com.feedhanjum.back_end.test.util.DomainTestUtils.createTeamWithoutId;
import static com.feedhanjum.back_end.test.util.SessionTestUtil.withLoginUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TeamControllerIntegrationTest {
    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private Clock clock;

    @Autowired
    private TeamRepository teamRepository;

    @MockitoBean
    private EventPublisher eventPublisher;

    private Member member1;
    private Member member2;
    private Member member3;
    @Autowired
    private TeamJoinTokenRepository teamJoinTokenRepository;

    @BeforeEach
    void setUp() {
        member1 = createMemberWithoutId("member1");
        member2 = createMemberWithoutId("member2");
        member3 = createMemberWithoutId("member3");
        memberRepository.saveAll(List.of(member1, member2, member3));
    }

    @Nested
    @DisplayName("팀 생성 api 테스트")
    class CreateTeam {
        @Test
        @DisplayName("성공 시 201")
        void test1() throws JsonProcessingException {
            // given
            Member leader = member1;
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now().plusDays(1);
            TeamCreateRequest request = new TeamCreateRequest("team1", startDate, endDate, FeedbackType.ANONYMOUS);

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/create")
                            .session(withLoginUser(leader))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request))
            ).hasStatus(HttpStatus.CREATED)
                    .body()
                    .satisfies(result -> {
                        TeamResponse response = mapper.readValue(result, TeamResponse.class);
                        assertThat(response.name()).isEqualTo("team1");
                        assertThat(response.startDate()).isEqualTo(startDate);
                        assertThat(response.endDate()).isEqualTo(endDate);
                        assertThat(response.feedbackType()).isEqualTo(FeedbackType.ANONYMOUS);

                        List<Team> teams = teamRepository.findAll();
                        assertThat(teams).hasSize(1);
                        assertThat(teams.get(0).getId()).isEqualTo(response.id());
                    });
        }
    }

    @Nested
    @DisplayName("팀 정보 수정 api 테스트")
    class UpdateTeamInfo {
        @Test
        @DisplayName("성공 시 200")
        void test1() throws JsonProcessingException {
            // given
            Member leader = member1;
            Team team = createTeamWithoutId("teamUpdate", leader);
            teamRepository.save(team);

            TeamUpdateRequest request = new TeamUpdateRequest("updateName", LocalDate.now(), LocalDate.now().plusDays(5), FeedbackType.IDENTIFIED);

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/{teamId}", team.getId())
                            .session(withLoginUser(leader))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request))
            ).hasStatus(HttpStatus.OK)
                    .body()
                    .satisfies(result -> {
                        TeamResponse response = mapper.readValue(result, TeamResponse.class);
                        assertThat(response.name()).isEqualTo("updateName");
                        assertThat(response.startDate()).isEqualTo(request.startDate());
                        assertThat(response.endDate()).isEqualTo(request.endDate());
                        assertThat(response.feedbackType()).isEqualTo(request.feedbackType());

                        Team updatedTeam = teamRepository.findById(team.getId()).orElseThrow();
                        assertThat(updatedTeam.getName()).isEqualTo("updateName");
                        assertThat(updatedTeam.getStartDate()).isEqualTo(request.startDate());
                        assertThat(updatedTeam.getEndDate()).isEqualTo(request.endDate());
                    });
        }

        @Test
        @DisplayName("팀장이 아닌 사용자가 요청한 경우 403")
        void test2() throws JsonProcessingException {
            // given
            Member leader = member1;
            Member nonLeader = member2;
            Team team = createTeamWithoutId("teamUpdate", leader);
            teamRepository.save(team);

            TeamUpdateRequest request = new TeamUpdateRequest("updateName", LocalDate.now(), LocalDate.now().plusDays(5), FeedbackType.ANONYMOUS);

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/{teamId}", team.getId())
                            .session(withLoginUser(nonLeader))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request))
            ).hasStatus(HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("존재하지 않는 팀 정보를 수정하려는 경우 404")
        void test3() throws JsonProcessingException {
            // given
            Member leader = member1;

            TeamUpdateRequest request = new TeamUpdateRequest("updateName", LocalDate.now(), LocalDate.now().plusDays(5), FeedbackType.IDENTIFIED);

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/{teamId}", Long.MAX_VALUE)
                            .session(withLoginUser(leader))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(request))
            ).hasStatus(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("팀 가입 토큰 발급 api 테스트")
    class CreateTeamJoinToken {
        @Test
        @DisplayName("성공 시 200")
        void test1() {
            // given
            Member member = member1;
            Team team = createTeamWithoutId("team1", member);
            teamRepository.save(team);

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/{teamId}/join-token", team.getId())
                            .session(withLoginUser(member))
                            .contentType(MediaType.APPLICATION_JSON)
            ).hasStatus(HttpStatus.OK)
                    .body()
                    .satisfies(result -> {
                        TeamJoinTokenResponse token = mapper.readValue(result, TeamJoinTokenResponse.class);
                        assertThat(token.validUntil()).isCloseTo(LocalDateTime.now().plusHours(TeamJoinToken.EXPIRATION_HOURS), within(1, ChronoUnit.SECONDS));

                        List<TeamJoinToken> tokens = teamJoinTokenRepository.findAll();
                        assertThat(tokens).hasSize(1);
                        assertThat(tokens.get(0).getToken()).isEqualTo(token.token());

                        assertThat(tokens.get(0).getExpireDate()).isEqualTo(token.validUntil());
                    });
        }

        @Test
        @DisplayName("팀원이 아닌 사용자가 요청 시 404")
        void test2() {
            // given
            Member leader = member1;
            Member nonTeamMember = member2;
            Team team = createTeamWithoutId("team1", leader);
            teamRepository.save(team);

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/{teamId}/join-token", team.getId())
                            .session(withLoginUser(nonTeamMember))
                            .contentType(MediaType.APPLICATION_JSON)
            ).hasStatus(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("존재하지 않는 팀에 대한 요청 시 404")
        void test3() {
            // given
            Member member = member1;

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/{teamId}/join-token", Long.MAX_VALUE)
                            .session(withLoginUser(member))
                            .contentType(MediaType.APPLICATION_JSON)
            ).hasStatus(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("팀 탈퇴 api 테스트")
    class LeaveTeam {
        @Test
        @DisplayName("성공 시 204")
        void test1() {
            // given
            Member leader = member1;
            Member member = member2;
            Team team = createTeamWithoutId("team1", leader);
            team.join(member);
            teamRepository.save(team);

            // when & then
            assertThat(
                    mockMvc.delete()
                            .uri("/api/team/{teamId}/leave", team.getId())
                            .session(withLoginUser(member))
            ).hasStatus(HttpStatus.NO_CONTENT);

            assertThat(team.isTeamMember(member)).isFalse();
            verify(eventPublisher).publishEvent(any(TeamMemberLeftEvent.class));
        }

        @Test
        @DisplayName("존재하지 않는 팀에서 탈퇴 시도 시 404")
        void test2() {
            // given
            Member member = member1;

            // when & then
            assertThat(
                    mockMvc.delete()
                            .uri("/api/team/{teamId}/leave", Long.MAX_VALUE)
                            .session(withLoginUser(member))
            ).hasStatus(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("팀장이 다른 팀원이 있는 상태에서 탈퇴 요청 시 400")
        void test3() {
            // given
            Member leader = member1;
            Member otherMember = member2;
            Team team = createTeamWithoutId("team1", leader);
            team.join(otherMember);
            teamRepository.save(team);

            // when & then
            assertThat(
                    mockMvc.delete()
                            .uri("/api/team/{teamId}/leave", team.getId())
                            .session(withLoginUser(leader))
            ).hasStatus(HttpStatus.BAD_REQUEST);

            boolean stillInTeam = teamRepository.findById(team.getId()).orElseThrow()
                    .getTeamMembers().stream()
                    .anyMatch(tm -> tm.getMember().equals(leader));
            assertThat(stillInTeam).isTrue();
        }
    }

    @Nested
    @DisplayName("내가 속한 팀 조회 api 테스트")
    class MyTeam {
        @Test
        @DisplayName("성공 시 200")
        void test1() {
            // given
            Member me = member1;
            Member notMe = member2;
            Team team1 = createTeamWithoutId("team1", me);
            Team team2 = createTeamWithoutId("team2", me);
            Team team3 = createTeamWithoutId("team3", notMe);
            teamRepository.saveAll(List.of(team1, team2, team3));


            // when & then
            assertThat(
                    mockMvc.get()
                            .uri("/api/team/my-teams")
                            .session(withLoginUser(me))
            ).hasStatus(HttpStatus.OK)
                    .body()
                    .satisfies(result -> {
                        List<TeamResponse> responses = mapper.readValue(result, new TypeReference<>() {
                        });
                        assertThat(responses).hasSize(2);
                        assertThat(responses).extracting(TeamResponse::name)
                                .containsExactlyInAnyOrder("team1", "team2");
                    });
        }
    }

    @Nested
    @DisplayName("팀 상세 정보 조회 api 테스트")
    class GetTeamDetail {
        @Test
        @DisplayName("성공 시 200")
        void test1() {
            // given
            Member me = member1;
            Member leader = member2;
            Team team = createTeamWithoutId("team1", leader);
            team.join(me);
            teamRepository.saveAll(List.of(team));

            // when & then
            assertThat(
                    mockMvc.get()
                            .uri("/api/team/{teamId}", team.getId())
                            .session(withLoginUser(me))
            ).hasStatus(HttpStatus.OK)
                    .body()
                    .satisfies(result -> {
                        TeamDetailResponse response = mapper.readValue(result, TeamDetailResponse.class);
                        assertThat(response.getTeamResponse().name()).isEqualTo("team1");
                        assertThat(response.getMembers()).extracting(MemberResponse::name)
                                .containsExactlyInAnyOrder(me.getName(), leader.getName());
                    });
        }

        @Test
        @DisplayName("해당 팀을 조회할 권한이 없는 경우 404")
        void test2() {
            // given
            Member me = member1;
            Member leader = member2;
            Team team = createTeamWithoutId("team1", leader);
            teamRepository.saveAll(List.of(team));

            // when & then
            assertThat(
                    mockMvc.get()
                            .uri("/api/team/{teamId}", team.getId())
                            .session(withLoginUser(me))
            ).hasStatus(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("팀 가입 토큰으로 팀 정보 조회 테스트")
    class GetTeamInfoByJoinToken {
        @Test
        @DisplayName("유효한 팀 가입 토큰으로 성공적으로 조회 시 200")
        void test1() {
            // given
            Member leader = member1;
            Team team = createTeamWithoutId("teamByJoinToken", leader);
            teamRepository.save(team);

            TeamJoinToken token = team.createJoinToken(leader, LocalDateTime.now(clock));
            teamJoinTokenRepository.save(token);

            // when & then
            assertThat(
                    mockMvc.get()
                            .uri("/api/team/find")
                            .queryParam("token", token.getToken())
            ).hasStatus(HttpStatus.OK)
                    .body()
                    .satisfies(result -> {
                        TeamResponse response = mapper.readValue(result, TeamResponse.class);
                        assertThat(response.id()).isEqualTo(team.getId());
                        assertThat(response.name()).isEqualTo("teamByJoinToken");
                    });
        }

        @Test
        @DisplayName("만료된 팀 가입 토큰으로 조회 시도 시 404")
        void test2() {
            // given
            Member leader = member1;
            Team team = createTeamWithoutId("teamByJoinToken", leader);
            teamRepository.save(team);

            TeamJoinToken expiredToken = team.createJoinToken(leader, LocalDateTime.now(clock));
            ReflectionTestUtils.setField(expiredToken, "expireDate", LocalDateTime.now().minusHours(1));
            teamJoinTokenRepository.save(expiredToken);


            // when & then
            assertThat(
                    mockMvc.get()
                            .uri("/api/team/find")
                            .queryParam("token", expiredToken.getToken())
            ).hasStatus(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("존재하지 않는 팀 가입 토큰으로 조회 시도 시 404")
        void test3() {
            // when & then
            assertThat(
                    mockMvc.get()
                            .uri("/api/team/find")
                            .queryParam("token", "1234")
            ).hasStatus(HttpStatus.NOT_FOUND);
        }
    }


    @Nested
    @DisplayName("팀원 조회 api 테스트")
    class GetTeamMembers {
        @Test
        @DisplayName("성공 시 200")
        void test1() {
            // given
            Member me = member1;
            Member otherMember = member2;
            Team team = createTeamWithoutId("team1", me);
            team.join(otherMember);
            teamRepository.save(team);

            // when & then
            assertThat(
                    mockMvc.get()
                            .uri("/api/team/{teamId}/members", team.getId())
                            .session(withLoginUser(me))
            ).hasStatus(HttpStatus.OK)
                    .body()
                    .satisfies(result -> {
                        List<MemberResponse> members = mapper.readValue(result, new TypeReference<>() {
                        });
                        assertThat(members).hasSize(2);
                        assertThat(members).extracting(MemberResponse::name)
                                .containsExactlyInAnyOrder(me.getName(), otherMember.getName());
                    });
        }

        @Test
        @DisplayName("팀원 조회 권한이 없는 경우 404")
        void test2() {
            // given
            Member me = member1;
            Member leader = member2;
            Team team = createTeamWithoutId("team1", leader);
            teamRepository.save(team);

            // when & then
            assertThat(
                    mockMvc.get()
                            .uri("/api/team/{teamId}/members", team.getId())
                            .session(withLoginUser(me))
            ).hasStatus(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("팀원 추방 api 테스트")
    class DeleteTeamMember {
        @Test
        @DisplayName("성공 시 204")
        void test1() {
            // given
            Member leader = member1;
            Member otherMember = member2;
            Team team = createTeamWithoutId("team1", leader);
            team.join(otherMember);
            teamRepository.save(team);

            // when & then
            assertThat(
                    mockMvc.delete()
                            .uri("/api/team/{teamId}/member/{removeMemberId}", team.getId(), otherMember.getId())
                            .session(withLoginUser(leader))
            ).hasStatus(HttpStatus.NO_CONTENT);

            List<Member> membersInTeam = teamRepository.findById(team.getId()).orElseThrow()
                    .getTeamMembers().stream().map(TeamMember::getMember).toList();
            assertThat(membersInTeam).hasSize(1);
            assertThat(membersInTeam.get(0).getId()).isEqualTo(leader.getId());
            verify(eventPublisher).publishEvent(any(TeamMemberLeftEvent.class));
        }

        @Test
        @DisplayName("팀장이 아닌 사용자가 요청한 경우 403")
        void test2() {
            // given
            Member leader = member1;
            Member otherMember = member2;
            Team team = createTeamWithoutId("team1", leader);
            team.join(otherMember);
            teamRepository.save(team);

            // when & then
            assertThat(
                    mockMvc.delete()
                            .uri("/api/team/{teamId}/member/{removeMemberId}", team.getId(), otherMember.getId())
                            .session(withLoginUser(otherMember))
            ).hasStatus(HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("존재하지 않는 팀에서 팀원을 추방하려는 경우 404")
        void test3() {
            // given
            Member leader = member1;

            // when & then
            assertThat(
                    mockMvc.delete()
                            .uri("/api/team/{teamId}/member/{removeMemberId}", Long.MAX_VALUE, member2.getId())
                            .session(withLoginUser(leader))
            ).hasStatus(HttpStatus.NOT_FOUND);
        }
    }


    @Nested
    @DisplayName("팀장 위임 api 테스트")
    class DelegateLeader {
        @Test
        @DisplayName("성공 시 200")
        void test1() throws JsonProcessingException {
            // given
            Member leader = member1;
            Member newLeader = member2;
            Team team = createTeamWithoutId("team1", leader);
            team.join(newLeader);
            teamRepository.save(team);

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/{teamId}/leader", team.getId())
                            .session(withLoginUser(leader))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(newLeader.getId()))
            ).hasStatus(HttpStatus.OK);

            Team updatedTeam = teamRepository.findById(team.getId()).orElseThrow();
            assertThat(updatedTeam.getLeader().getId()).isEqualTo(newLeader.getId());
        }

        @Test
        @DisplayName("팀장이 아닌 사용자가 요청한 경우 403")
        void test2() throws JsonProcessingException {
            // given
            Member leader = member1;
            Member nonLeader = member2;
            Member newLeader = member3;
            Team team = createTeamWithoutId("team1", leader);
            team.join(nonLeader);
            team.join(newLeader);
            teamRepository.save(team);

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/{teamId}/leader", team.getId())
                            .session(withLoginUser(nonLeader))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(newLeader.getId()))
            ).hasStatus(HttpStatus.FORBIDDEN);

            Team unchangedTeam = teamRepository.findById(team.getId()).orElseThrow();
            assertThat(unchangedTeam.getLeader().getId()).isEqualTo(leader.getId());
        }

        @Test
        @DisplayName("존재하지 않는 팀 또는 팀원에 대한 요청인 경우 404")
        void test3() throws JsonProcessingException {
            // given
            Member leader = member1;
            teamRepository.save(createTeamWithoutId("team1", leader));

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/{teamId}/leader", Long.MAX_VALUE)
                            .session(withLoginUser(leader))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(Long.MAX_VALUE))
            ).hasStatus(HttpStatus.NOT_FOUND);
        }
    }


    @Nested
    @DisplayName("팀 가입 api 테스트")
    class JoinTeam {
        @Test
        @DisplayName("팀 가입 성공 시 204")
        void test1() {
            // given
            Member leader = member1;
            Team team = createTeamWithoutId("team1", leader);
            teamRepository.save(team);

            TeamJoinToken token = team.createJoinToken(leader, LocalDateTime.now(clock));
            teamJoinTokenRepository.save(token);

            Member notMember = member2;

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/join")
                            .queryParam("token", token.getToken())
                            .session(withLoginUser(notMember))
            ).hasStatus(HttpStatus.NO_CONTENT);

            Team updatedTeam = teamRepository.findById(team.getId()).orElseThrow();
            assertThat(updatedTeam.isTeamMember(notMember)).isTrue();
        }

        @Test
        @DisplayName("유효하지 않은 토큰으로 요청 시 404")
        void test2() {
            // given
            Member leader = member1;
            Team team = createTeamWithoutId("team1", leader);
            teamRepository.save(team);

            TeamJoinToken token = team.createJoinToken(leader, LocalDateTime.now(clock));
            teamJoinTokenRepository.save(token);

            Member notMember = member2;

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/join")
                            .queryParam("token", "invalidToken")
                            .session(withLoginUser(notMember))
            ).hasStatus(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("만료된 토큰으로 요청 시 404")
        void test3() {
            // given
            Member leader = member1;
            Team team = createTeamWithoutId("team1", leader);
            teamRepository.save(team);

            TeamJoinToken expiredToken = team.createJoinToken(leader, LocalDateTime.now(clock));
            ReflectionTestUtils.setField(expiredToken, "expireDate", LocalDateTime.now().minusHours(1));
            teamJoinTokenRepository.save(expiredToken);

            Member notMember = member2;

            // when & then
            assertThat(
                    mockMvc.post()
                            .uri("/api/team/join")
                            .queryParam("token", expiredToken.getToken())
                            .session(withLoginUser(notMember)) // Assume member2 is trying to join
                            .contentType(MediaType.APPLICATION_JSON)
            ).hasStatus(HttpStatus.NOT_FOUND);
        }
    }
}
