package com.feedhanjum.back_end.notification.service;

import com.feedhanjum.back_end.core.domain.JobRecord;
import com.feedhanjum.back_end.core.repository.JobRecordRepository;
import com.feedhanjum.back_end.event.EventPublisher;
import com.feedhanjum.back_end.feedback.domain.Feedback;
import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.notification.domain.FeedbackReceiveNotification;
import com.feedhanjum.back_end.notification.domain.UnreadFeedbackExistNotification;
import com.feedhanjum.back_end.notification.event.InAppNotificationCreatedEvent;
import com.feedhanjum.back_end.notification.repository.InAppNotificationQueryRepository;
import com.feedhanjum.back_end.notification.repository.InAppNotificationRepository;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.team.repository.TeamRepository;
import com.feedhanjum.back_end.test.util.DomainTestUtils;
import jakarta.transaction.Transactional;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.feedhanjum.back_end.test.util.DomainTestUtils.createMemberWithoutId;
import static com.feedhanjum.back_end.test.util.DomainTestUtils.createTeamWithoutId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class InAppNotificationServiceIntegrationTest {
    @MockitoBean
    private EventPublisher eventPublisher;

    @Autowired
    private JobRecordRepository jobRecordRepository;

    @Autowired
    private InAppNotificationQueryRepository inAppNotificationQueryRepository;

    @Autowired
    private InAppNotificationRepository inAppNotificationRepository;

    @Autowired
    private InAppNotificationService inAppNotificationService;
    @Autowired
    private Clock clock;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;
    private Member receiver;
    private Team team;
    private Member sender1;
    private Member sender2;
    private Member sender3;


    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        public Clock fixedClock() {
            return Clock.fixed(Instant.parse("2025-01-10T12:00:00Z"), ZoneId.systemDefault());
        }
    }

    @BeforeEach
    void setup() {
        sender1 = memberRepository.save(createMemberWithoutId("sender1"));
        sender2 = memberRepository.save(createMemberWithoutId("sender2"));
        sender3 = memberRepository.save(createMemberWithoutId("sender3"));
        receiver = memberRepository.save(createMemberWithoutId("receiver"));
        team = teamRepository.save(createTeamWithoutId("team", receiver));
        team.join(sender1);
        team.join(sender2);
        team.join(sender3);
    }

    private FeedbackReceiveNotification createFeedbackReceiveNotification(LocalDateTime createdAt, Member sender) {
        Feedback feedbackWithId = DomainTestUtils.createFeedbackWithId(sender, receiver, team, FeedbackType.IDENTIFIED);
        FeedbackReceiveNotification notification = new FeedbackReceiveNotification(feedbackWithId);
        ReflectionTestUtils.setField(notification, "createdAt", createdAt);
        return notification;
    }

    @Test
    @DisplayName("같은 사용자에게 여러개의 안읽은 알림이 있으면 가장 오래된 것을 이용해 미확인 알림 생성")
    void test1() {
        // given
        LocalDateTime previousFinishTime = LocalDateTime.now(clock).minusDays(1).minusMinutes(1);
        jobRecordRepository.save(new JobRecord(JobRecord.JobName.UNREAD_NOTIFICATIONS, previousFinishTime));
        inAppNotificationRepository.saveAll(List.of(
                createFeedbackReceiveNotification(previousFinishTime.plusSeconds(3), sender3),
                createFeedbackReceiveNotification(previousFinishTime.plusSeconds(1), sender1),
                createFeedbackReceiveNotification(previousFinishTime.plusSeconds(2), sender2)
        ));


        // when
        inAppNotificationService.checkUnreadNotifications();

        // then
        assertThat(inAppNotificationRepository.findAll())
                .filteredOn(UnreadFeedbackExistNotification.class::isInstance)
                .hasSize(1)
                .first()
                .asInstanceOf(InstanceOfAssertFactories.type(UnreadFeedbackExistNotification.class))
                .satisfies(notification -> {
                    assertThat(notification.getSenderName()).isEqualTo(sender1.getName());
                    verify(eventPublisher).publishEvent(new InAppNotificationCreatedEvent(notification.getId()));
                });


    }

    @Test
    @DisplayName("사용자에게 이미 미확인 알림이 있다면 추가 생성 없음")
    void test2() {
        LocalDateTime previousFinishTime = LocalDateTime.now(clock).minusDays(1).minusMinutes(1);

        UnreadFeedbackExistNotification existNotification = inAppNotificationRepository.save(
                new UnreadFeedbackExistNotification(
                        createFeedbackReceiveNotification(previousFinishTime.minusSeconds(3), sender3)
                )
        );

        jobRecordRepository.save(new JobRecord(JobRecord.JobName.UNREAD_NOTIFICATIONS, previousFinishTime));
        inAppNotificationRepository.saveAll(List.of(
                createFeedbackReceiveNotification(previousFinishTime.plusSeconds(3), sender3),
                createFeedbackReceiveNotification(previousFinishTime.plusSeconds(1), sender1),
                createFeedbackReceiveNotification(previousFinishTime.plusSeconds(2), sender2)
        ));


        // when
        inAppNotificationService.checkUnreadNotifications();

        // then
        assertThat(inAppNotificationRepository.findAll())
                .filteredOn(UnreadFeedbackExistNotification.class::isInstance)
                .hasSize(1)
                .first()
                .asInstanceOf(InstanceOfAssertFactories.type(UnreadFeedbackExistNotification.class))
                .satisfies(notification -> {
                    assertThat(notification.getId()).isEqualTo(existNotification.getId());
                });
        verify(eventPublisher, never()).publishEvent(any());
    }


}