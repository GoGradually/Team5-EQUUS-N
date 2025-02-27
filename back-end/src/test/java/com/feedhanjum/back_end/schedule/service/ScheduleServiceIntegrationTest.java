package com.feedhanjum.back_end.schedule.service;

import com.feedhanjum.back_end.core.domain.JobRecord;
import com.feedhanjum.back_end.core.event.EventPublisher;
import com.feedhanjum.back_end.core.repository.JobRecordRepository;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.schedule.domain.Schedule;
import com.feedhanjum.back_end.schedule.event.ScheduleEndedEvent;
import com.feedhanjum.back_end.schedule.repository.ScheduleRepository;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.team.repository.TeamRepository;
import com.feedhanjum.back_end.test.util.DomainTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class ScheduleServiceIntegrationTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JobRecordRepository jobRecordRepository;

    @Autowired
    private Clock clock;

    @MockitoBean
    private EventPublisher eventPublisher;

    @Autowired
    private ScheduleService scheduleService;

    private Member member1;
    private Member member2;
    private Member member3;
    private Team team1;
    private Team team2;

    @TestConfiguration
    static class Config {

        @Bean
        @Primary
        public Clock fixedClock() {
            return Clock.fixed(Instant.parse("2020-01-01T00:00:00Z"), ZoneId.systemDefault());

        }

    }


    @BeforeEach
    void setup() {
        member1 = DomainTestUtils.createMemberWithoutId("member1");
        member2 = DomainTestUtils.createMemberWithoutId("member2");
        member3 = DomainTestUtils.createMemberWithoutId("member3");
        memberRepository.saveAll(List.of(member1, member2, member3));

        team1 = DomainTestUtils.createTeamWithoutId("team1", member1);
        team1.join(member2);
        team1.join(member3);
        team2 = DomainTestUtils.createTeamWithoutId("team2", member2);
        team2.join(member1);
        team2.join(member3);
        teamRepository.saveAll(List.of(team1, team2));

    }


    @Nested
    @DisplayName("일정 종료 로직 테스트")
    class EndTeamTest {
        // 일정 종료 로직 테스트

        @Test
        @DisplayName("이전 종료 시간과 현재 시간 사이의 팀들만 처리")
        void endTeam() {
            // given
            LocalDateTime now = LocalDateTime.now(clock);

            List<Schedule> targetSchedules = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                targetSchedules.add(
                        new Schedule("schedule" + i, now.minusMinutes(i * 10 + 20), now.minusMinutes(i * 10 + 10), team1, member1)
                );
            }
            scheduleRepository.saveAll(targetSchedules);

            LocalDateTime previousEndDateTime = now.minusDays(1);
            JobRecord jobRecord = new JobRecord(JobRecord.JobName.SCHEDULE, previousEndDateTime);
            jobRecordRepository.save(jobRecord);

            List<Schedule> alreadyEndedSchedules = List.of(
                    new Schedule("ended-schedule1", previousEndDateTime.minusMinutes(20), previousEndDateTime.minusMinutes(10), team1, member1),
                    new Schedule("ended-schedule2", previousEndDateTime.minusMinutes(30), previousEndDateTime.minusMinutes(20), team1, member1)
            );
            scheduleRepository.saveAll(alreadyEndedSchedules);

            List<Schedule> notEndedSchedules = List.of(
                    new Schedule("not-ended-schedule1", now, now.plusMinutes(10), team1, member1),
                    new Schedule("not-ended-schedule2", now.plusMinutes(10), now.plusMinutes(20), team1, member1)
            );
            scheduleRepository.saveAll(notEndedSchedules);

            // when

            scheduleService.endSchedules();


            // then
            assertThat(jobRecordRepository.findAll().get(0).getPreviousFinishTime()).isEqualTo(now);
            ArgumentCaptor<ScheduleEndedEvent> captor = ArgumentCaptor.captor();
            verify(eventPublisher, times(10)).publishEvent(captor.capture());
            List<ScheduleEndedEvent> events = captor.getAllValues();
            assertThat(events).allMatch(event ->
                    targetSchedules.stream()
                            .map(Schedule::getId).toList()
                            .contains(event.scheduleId()));
            assertThat(events).noneMatch(event ->
                    alreadyEndedSchedules.stream()
                            .map(Schedule::getId).toList()
                            .contains(event.scheduleId()));
            assertThat(events).noneMatch(event ->
                    notEndedSchedules.stream()
                            .map(Schedule::getId).toList()
                            .contains(event.scheduleId()));
        }
    }
}