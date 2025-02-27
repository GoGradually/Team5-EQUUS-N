package com.feedhanjum.back_end.member.service;

import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.domain.ProfileImage;
import com.feedhanjum.back_end.member.repository.MemberQueryRepository;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.team.domain.TeamMember;
import com.feedhanjum.back_end.team.exception.TeamMembershipNotFoundException;
import com.feedhanjum.back_end.team.repository.TeamMemberRepository;
import com.feedhanjum.back_end.team.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private MemberQueryRepository memberQueryRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 아이디로 회원을 정상적으로 조회한다")
    void getMemberById_정상조회() {
        //given
        Long memberId = 1L;
        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        Member member = new Member("홍길동", "hong@example.com", new ProfileImage("blue", "img.png"), feedbackPreferences);
        ReflectionTestUtils.setField(member, "id", memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        //when
        Member result = memberService.getMemberById(memberId);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(memberId);
        assertThat(result.getName()).isEqualTo("홍길동");
        assertThat(result.getEmail()).isEqualTo("hong@example.com");
        assertThat(result.getProfileImage().getBackgroundColor()).isEqualTo("blue");
        assertThat(result.getProfileImage().getImage()).isEqualTo("img.png");
    }

    @Test
    @DisplayName("존재하지 않는 회원 아이디 조회 시 예외를 발생시킨다")
    void getMemberById_회원없음() {
        //given
        Long memberId = 2L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> memberService.getMemberById(memberId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("찾으려는 해당 사용자가 없습니다.");
    }

    @Test
    @DisplayName("회원 정보 변경에 성공한다")
    void changeProfile() {
        // given
        Long memberId = 1L;
        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        Member member = new Member("haha", "hoho", new ProfileImage("huhu", "hehe"), feedbackPreferences);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        String newName = "hoho";
        ProfileImage newProfileImage = new ProfileImage("hehe", "haha");
        // when
        Member result = memberService.changeProfile(memberId, newName, newProfileImage);
        // then
        assertThat(result.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("팀 내 회원 조회 성공")
    void getMembersByTeam_조회성공() {
        //given
        Long memberId = 1L;
        Long teamId = 1L;
        TeamMember teamMember = mock(TeamMember.class);
        when(teamMemberRepository.findByMemberIdAndTeamId(memberId, teamId))
                .thenReturn(Optional.of(teamMember));
        Member member = mock(Member.class);
        List<Member> expectedMembers = List.of(member);
        Team team = mock(Team.class);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberQueryRepository.findMembersByTeamId(teamId))
                .thenReturn(expectedMembers);

        //when
        List<Member> result = memberService.getMembersByTeam(memberId, teamId);

        //then
        assertThat(result).isEqualTo(expectedMembers);
    }

    @Test
    @DisplayName("가입되지 않은 팀 조회 시 예외 발생")
    void getMembersByTeam_팀미가입예외() {
        //given
        Long memberId = 1L;
        Long teamId = 1L;
        Member member = mock(Member.class);
        Team team = mock(Team.class);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(teamMemberRepository.findByMemberIdAndTeamId(memberId, teamId))
                .thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> memberService.getMembersByTeam(memberId, teamId))
                .isInstanceOf(TeamMembershipNotFoundException.class)
                .hasMessage("속해있는 팀에 대한 정보만 접근 가능합니다.");
    }
}
