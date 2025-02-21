package com.feedhanjum.back_end.member.controller;

import com.feedhanjum.back_end.member.controller.dto.MemberResponse;
import com.feedhanjum.back_end.member.controller.dto.ProfileChangeRequest;
import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.domain.ProfileImage;
import com.feedhanjum.back_end.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    @Test
    @DisplayName("회원 아이디로 회원 정보를 정상적으로 조회한다")
    void getMemberById_정상조회() {
        //given
        Long memberId = 1L;
        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        Member member = new Member("홍길동", "hong@example.com", new ProfileImage("blue", "img.png"), feedbackPreferences);
        ReflectionTestUtils.setField(member, "id", memberId);
        when(memberService.getMemberById(memberId)).thenReturn(member);

        //when
        ResponseEntity<MemberResponse> result = memberController.getMemberById(memberId);

        //then
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().id()).isEqualTo(memberId);
        assertThat(result.getBody().name()).isEqualTo("홍길동");
        assertThat(result.getBody().email()).isEqualTo("hong@example.com");
        assertThat(result.getBody().profileImage().getBackgroundColor()).isEqualTo("blue");
        assertThat(result.getBody().profileImage().getImage()).isEqualTo("img.png");
    }

    @Test
    @DisplayName("회원 정보 변경 컨트롤러 동작 확인")
    void changeProfile_프로필변경() {
        // given
        Long memberId = 1L;
        String newName = "hoho";
        ProfileImage profileImage = new ProfileImage("haha", "hehe");
        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        Member member = new Member(newName, "hoho", new ProfileImage("huhu", "hehe"), feedbackPreferences);
        when(memberService.changeProfile(memberId, newName, profileImage)).thenReturn(member);
        ProfileChangeRequest profileChangeRequest = new ProfileChangeRequest(newName, profileImage);
        // when
        ResponseEntity<MemberResponse> response = memberController.changeProfile(memberId, profileChangeRequest);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        MemberResponse memberResponse = response.getBody();
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.name()).isEqualTo(newName);
    }
}
