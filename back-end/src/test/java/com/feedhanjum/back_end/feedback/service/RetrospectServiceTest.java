package com.feedhanjum.back_end.feedback.service;

import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.feedback.domain.Retrospect;
import com.feedhanjum.back_end.feedback.repository.RetrospectQueryRepository;
import com.feedhanjum.back_end.feedback.repository.RetrospectRepository;
import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.domain.ProfileImage;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.team.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static com.feedhanjum.back_end.test.util.DomainTestUtils.assertEqualTeam;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetrospectServiceTest {
    private static final int PAGE_SIZE = 10;
    @Mock
    private RetrospectRepository retrospectRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private RetrospectQueryRepository retrospectQueryRepository;
    @InjectMocks
    private RetrospectService retrospectService;

    private final AtomicLong nextId = new AtomicLong(1L);

    private Member createMember(String name) {
        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        Member member = new Member(name, name + "@email.com", new ProfileImage("bg " + name, "image " + name), feedbackPreferences);
        ReflectionTestUtils.setField(member, "id", nextId.getAndIncrement());
        return member;
    }

    private Team createTeam(String teamName, Member leader) {
        Team team = new Team(teamName, leader, LocalDate.now(), LocalDate.now().plusDays(1), FeedbackType.ANONYMOUS, LocalDate.now());
        ReflectionTestUtils.setField(team, "id", nextId.getAndIncrement());
        return team;
    }

    private Retrospect createRetrospect(String title, Member writer, Team team) {
        Retrospect retrospect = new Retrospect(title, title + "'s content", writer, team);
        ReflectionTestUtils.setField(retrospect, "id", nextId.getAndIncrement());
        return retrospect;
    }

    @Test
    @DisplayName("회고 작성 성공")
    void test1() {
        // given
        String title = "title";
        String content = "content";
        Long writerId = 1L;
        Long teamId = 1L;

        Member writer = mock();
        Team team = mock();

        when(memberRepository.findById(writerId)).thenReturn(Optional.of(writer));
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // when
        Retrospect result = retrospectService.writeRetrospect(title, content, writerId, teamId);

        // then
        verify(retrospectRepository, times(1)).save(any());
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getWriter()).isEqualTo(writer);
        assertEqualTeam(team, result.getTeam());
    }

    @Test
    @DisplayName("회고 작성 실패 - writerId에 해당하는 Member가 없을 때")
    void test2() {
        // given
        String title = "title";
        String content = "content";
        Long writerId = 1L;
        Long teamId = 1L;

        when(memberRepository.findById(writerId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> retrospectService.writeRetrospect(title, content, writerId, teamId))
                .isInstanceOf(EntityNotFoundException.class);

    }

    @Test
    @DisplayName("회고 작성 실패 - teamId에 해당하는 Team이 없을 때")
    void test3() {
        // given
        String title = "title";
        String content = "content";
        Long writerId = 1L;
        Long teamId = 1L;

        when(memberRepository.findById(writerId)).thenReturn(Optional.of(mock()));
        when(teamRepository.findById(writerId)).thenReturn(Optional.empty());


        // when & then
        assertThatThrownBy(() -> retrospectService.writeRetrospect(title, content, writerId, teamId))
                .isInstanceOf(EntityNotFoundException.class);

    }

    @Test
    @DisplayName("회고 조회 성공")
    void test4() {
        // given
        int page = 1;
        Member writer = createMember("member1");
        Team team = createTeam("team1", writer);
        Sort.Direction sortOrder = Sort.Direction.DESC;
        ArgumentCaptor<PageRequest> pageCaptor = ArgumentCaptor.captor();
        Page<Retrospect> result = new PageImpl<>(List.of(
                createRetrospect("retrospect1", writer, team),
                createRetrospect("retrospect2", writer, team)
        ));

        when(retrospectQueryRepository.findRetrospects(eq(writer.getId()), eq(team.getId()), pageCaptor.capture(), eq(sortOrder))).thenReturn(result);

        // when
        Page<Retrospect> retrospects = retrospectService.getRetrospects(writer.getId(), team.getId(), page, sortOrder);

        // then
        assertThat(retrospects).isEqualTo(result);

        Pageable pageable = pageCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(page);
        assertThat(pageable.getPageSize()).isEqualTo(PAGE_SIZE);
    }

    @Test
    @DisplayName("회고 조회 실패 - page가 음수일 때")
    void test8() {
        // given
        int page = -1;
        Long writerId = 1L;
        Long teamId = 2L;

        // when & then
        assertThatThrownBy(() -> retrospectService.getRetrospects(writerId, teamId, page, Sort.Direction.ASC))
                .isInstanceOf(IllegalArgumentException.class);
    }
}