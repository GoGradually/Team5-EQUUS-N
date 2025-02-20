package com.feedhanjum.back_end.schedule.service;

import com.feedhanjum.back_end.event.EventPublisher;
import com.feedhanjum.back_end.member.repository.MemberQueryRepository;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.schedule.repository.ScheduleMemberRepository;
import com.feedhanjum.back_end.schedule.repository.ScheduleQueryRepository;
import com.feedhanjum.back_end.schedule.repository.ScheduleRepository;
import com.feedhanjum.back_end.team.repository.TeamMemberRepository;
import com.feedhanjum.back_end.team.repository.TeamRepository;
import com.feedhanjum.back_end.teamplanorchestration.service.TeamPlanOrchestrationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    Clock clock;

    @Mock
    TeamRepository teamRepository;

    @Mock
    TeamMemberRepository teamMemberRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    ScheduleMemberRepository scheduleMemberRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ScheduleQueryRepository scheduleQueryRepository;

    @Mock
    EventPublisher eventPublisher;

    @Mock
    MemberQueryRepository memberQueryRepository;

    @InjectMocks
    TeamPlanOrchestrationService teamPlanOrchestrationService;

    @InjectMocks
    ScheduleService scheduleService;


}
