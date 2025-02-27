package com.feedhanjum.back_end.feedback.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedhanjum.back_end.auth.infra.SessionConst;
import com.feedhanjum.back_end.core.dto.Paged;
import com.feedhanjum.back_end.feedback.controller.dto.request.RetrospectWriteRequest;
import com.feedhanjum.back_end.feedback.controller.dto.response.RetrospectResponse;
import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.feedback.domain.Retrospect;
import com.feedhanjum.back_end.feedback.repository.RetrospectRepository;
import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.domain.ProfileImage;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.team.domain.TeamMember;
import com.feedhanjum.back_end.team.repository.TeamMemberRepository;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.feedhanjum.back_end.test.util.DomainTestUtils.assertEqualTeam;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
class RetrospectControllerTest {
    @Autowired
    private MockMvcTester mvc;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RetrospectRepository retrospectRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamMemberRepository teamMemberRepository;
    private final Clock clock = Clock.fixed(Instant.parse("2025-01-10T12:00:00Z"), ZoneId.systemDefault());


    private Member createMember(String name) {
        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        return new Member(name, name + "@test.com", new ProfileImage("bg-" + name, "profile-" + name), feedbackPreferences);
    }

    private Team createTeam(String name, Member leader) {
        return new Team(name, leader, LocalDate.now(clock).minusDays(1), LocalDate.now(clock).plusDays(1), FeedbackType.ANONYMOUS, LocalDate.now(clock));
    }

    private Retrospect createRetrospect(String title, Member writer, Team team) {
        return new Retrospect(title, title + "'s content", writer, team);
    }

    private Member member1;
    private Member member2;
    private Member member3;
    private Team team1;
    private Team team2;

    @BeforeEach
    void setup() {
        member1 = createMember("member1");
        member2 = createMember("member2");
        member3 = createMember("member3");
        memberRepository.saveAll(List.of(member1, member2, member3));

        team1 = createTeam("team1", member1);
        team2 = createTeam("team2", member2);
        teamRepository.saveAll(List.of(team1, team2));

        teamMemberRepository.saveAll(List.of(
                new TeamMember(team1, member1),
                new TeamMember(team1, member2),
                new TeamMember(team1, member3),
                new TeamMember(team2, member1),
                new TeamMember(team2, member2),
                new TeamMember(team2, member3)
        ));
    }

    MockHttpSession withLoginUser(Member member) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.MEMBER_ID, member.getId());
        return session;
    }

    @Nested
    @DisplayName("회고 조회하기 테스트")
    class GetRetrospectsTest {
        @Test
        @DisplayName("성공 시 200")
        void test1() {
            // given
            Member writer = member2;
            Team team = team2;
            Retrospect retrospect1 = createRetrospect("retrospect1", writer, team);
            Retrospect retrospect2 = createRetrospect("retrospect2", writer, team);
            retrospectRepository.saveAll(List.of(
                    retrospect1, retrospect2));

            // when
            assertThat(mvc.get()
                    .uri("/api/retrospect/{writerId}", writer.getId())
                    .session(withLoginUser(writer))
            ).hasStatusOk();
        }

        @Test
        @DisplayName("팀으로 조회")
        void test2() {
            // given
            Member writer = member2;
            Team team = team1;
            Team otherTeam = team2;
            Retrospect retrospect1 = createRetrospect("retrospect1", writer, team);
            Retrospect retrospect2 = createRetrospect("retrospect2", writer, otherTeam);
            Retrospect retrospect3 = createRetrospect("retrospect3", writer, team);
            retrospectRepository.saveAll(List.of(
                    retrospect1, retrospect2, retrospect3));

            // when
            assertThat(mvc.get()
                    .uri("/api/retrospect/{writerId}", writer.getId())
                    .param("teamId", team.getId().toString())
                    .session(withLoginUser(writer))
            ).hasStatusOk()
                    .body()
                    .satisfies(result -> {
                        Paged<RetrospectResponse> responses = mapper.readValue(result, new TypeReference<>() {
                        });
                        assertThat(responses.content()).hasSize(2);
                        assertThat(responses.content()).extracting(RetrospectResponse::teamName)
                                .containsOnly(team.getName());
                    });
        }

        @Test
        @DisplayName("페이지로 조회")
        void test3() {
            // given
            Member writer = member2;
            Team team = team1;
            List<Retrospect> retrospects = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                retrospects.add(createRetrospect("title" + i, writer, team));
            }
            retrospectRepository.saveAll(retrospects);

            // when
            assertThat(mvc.get()
                    .uri("/api/retrospect/{writerId}", writer.getId())
                    .param("page", "1")
                    .session(withLoginUser(writer))
            ).hasStatusOk()
                    .body()
                    .satisfies(result -> {
                        Paged<RetrospectResponse> responses = mapper.readValue(result, new TypeReference<>() {
                        });
                        assertThat(responses.page()).isEqualTo(1);
                        assertThat(responses.hasNext()).isFalse();
                        System.out.println(responses.content());
                        assertThat(responses.content()).hasSize(10)
                                .extracting(RetrospectResponse::createdAt)
                                .isSortedAccordingTo(Comparator.reverseOrder());
                    });
        }

        @Test
        @DisplayName("정렬로 조회")
        void test4() {
            // given
            Member writer = member2;
            Team team = team1;
            List<Retrospect> retrospects = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                retrospects.add(createRetrospect("title" + i, writer, team));
            }
            retrospectRepository.saveAll(retrospects);

            // when
            assertThat(mvc.get()
                    .uri("/api/retrospect/{writerId}", writer.getId())
                    .param("sortOrder", "ASC")
                    .session(withLoginUser(writer))
            ).hasStatusOk()
                    .body()
                    .satisfies(result -> {
                        Paged<RetrospectResponse> responses = mapper.readValue(result, new TypeReference<>() {
                        });
                        assertThat(responses.content()).hasSize(10)
                                .extracting(RetrospectResponse::createdAt)
                                .isSortedAccordingTo(Comparator.naturalOrder());
                    });
        }

        @Test
        @DisplayName("본인이 아닌 경우 403")
        void test5() {
            // given
            Member writer = member2;
            Member notWriter = member1;
            Team team = team1;
            Retrospect retrospect1 = createRetrospect("retrospect1", writer, team);
            Retrospect retrospect2 = createRetrospect("retrospect2", writer, team);
            retrospectRepository.saveAll(List.of(
                    retrospect1, retrospect2));

            // when
            assertThat(mvc.get()
                    .uri("/api/retrospect/{writerId}", writer.getId())
                    .session(withLoginUser(notWriter))
            ).hasStatus(HttpStatus.FORBIDDEN);
        }
    }

    @Nested
    @DisplayName("회고 작성하기 테스트")
    class WriteRetrospectTest {
        @Test
        @DisplayName("성공 시 201")
        void test1() throws JsonProcessingException {
            // given
            Member writer = member2;
            Team team = team1;

            // when
            assertThat(mvc.post()
                    .uri("/api/retrospect")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(new RetrospectWriteRequest(writer.getId(), team.getId(), "title", "content")))
                    .session(withLoginUser(writer))
            ).hasStatus(HttpStatus.CREATED);

            // then
            List<Retrospect> retrospects = retrospectRepository.findAll();
            assertThat(retrospects).hasSize(1);
            Retrospect retrospect = retrospects.get(0);
            assertThat(retrospect.getTitle()).isEqualTo("title");
            assertThat(retrospect.getContent()).isEqualTo("content");
            assertThat(retrospect.getWriter()).isEqualTo(writer);
            assertEqualTeam(team, retrospect.getTeam());
        }

        @Test
        @DisplayName("본인이 아닌 경우 403")
        void test2() throws JsonProcessingException {
            // given
            Member writer = member2;
            Member notWriter = member1;
            Team team = team1;

            // when
            assertThat(mvc.post()
                    .uri("/api/retrospect")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(new RetrospectWriteRequest(writer.getId(), team.getId(), "title", "content")))
                    .session(withLoginUser(notWriter))
            ).hasStatus(HttpStatus.FORBIDDEN);

            // then
            assertThat(retrospectRepository.findAll()).isEmpty();
        }
    }

}