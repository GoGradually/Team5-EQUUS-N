package com.feedhanjum.back_end.notification.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedhanjum.back_end.auth.infra.SessionConst;
import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.domain.ProfileImage;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.notification.controller.dto.MultipleNotificationReadRequest;
import com.feedhanjum.back_end.notification.controller.dto.notification.InAppNotificationDto;
import com.feedhanjum.back_end.notification.domain.FeedbackReportCreateNotification;
import com.feedhanjum.back_end.notification.domain.InAppNotification;
import com.feedhanjum.back_end.notification.repository.InAppNotificationRepository;
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

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
class InAppNotificationControllerTest {

    @Autowired
    private MockMvcTester mvc;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InAppNotificationRepository notificationRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Member member1;
    private Member member2;
    @Autowired
    private TeamRepository teamRepository;
    private Team team1;
    @Autowired
    private TeamMemberRepository teamMemberRepository;


    private Member createMember(String name) {
        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        return new Member(name, name + "@test.com", new ProfileImage("bg-" + name, "profile-" + name), feedbackPreferences);
    }

    private Team createTeam(String name, Member leader) {
        return new Team(name, leader, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), FeedbackType.ANONYMOUS, LocalDate.now());
    }

    private InAppNotification createInAppNotification(Member receiver) {
        return new FeedbackReportCreateNotification(receiver);
    }

    MockHttpSession withLoginUser(Member member) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.MEMBER_ID, member.getId());
        return session;
    }

    @BeforeEach
    void setUp() {
        member1 = createMember("member1");
        member2 = createMember("member1");
        memberRepository.saveAll(List.of(member1, member2));

        team1 = createTeam("team1", member1);
        teamRepository.saveAll(List.of(team1));

        teamMemberRepository.saveAll(List.of(
                new TeamMember(team1, member1),
                new TeamMember(team1, member2)
        ));
    }

    @Nested
    @DisplayName("알림 조회 테스트")
    class GetAllNotifications {
        @Test
        @DisplayName("성공시 200")
        void test1() {
            Member receiver = member1;
            Member notReceiver = member2;
            List<InAppNotification> notifications = List.of(
                    createInAppNotification(receiver),
                    createInAppNotification(notReceiver),
                    createInAppNotification(receiver)
            );
            notificationRepository.saveAll(notifications);

            assertThat(mvc.get()
                    .uri("/api/notification")
                    .session(withLoginUser(receiver))
            ).hasStatusOk()
                    .body().satisfies(result -> {
                        List<InAppNotificationDto> response = mapper.readValue(result, new TypeReference<>() {
                        });
                        assertThat(response).hasSize(2);
                        assertThat(response).extracting(InAppNotificationDto::getCreatedAt).isSortedAccordingTo(Comparator.reverseOrder());
                    });
        }

        @Nested
        @DisplayName("여러 알림 읽음 처리 테스트")
        class MarkNotificationAsRead {
            @Test
            @DisplayName("성공시 204")
            void test1() throws JsonProcessingException {
                Member receiver = member1;
                List<InAppNotification> notifications = List.of(
                        createInAppNotification(receiver),
                        createInAppNotification(receiver),
                        createInAppNotification(receiver)
                );
                notificationRepository.saveAll(notifications);

                MultipleNotificationReadRequest request = new MultipleNotificationReadRequest(notifications.stream().map(InAppNotification::getId).toList());

                assertThat(mvc.post()
                        .uri("/api/notification/mark-as-read")
                        .session(withLoginUser(receiver))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(request))
                ).hasStatus(HttpStatus.NO_CONTENT);

                List<InAppNotification> all = notificationRepository.findAll();
                assertThat(all).allMatch(InAppNotification::isRead);
            }
        }
    }
}