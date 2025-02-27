package com.feedhanjum.back_end.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("회원 생성 시 필드가 정상적으로 초기화되는지 테스트")
    void memberConstructor_회원생성() {
        // given
        ProfileImage profileImage = new ProfileImage("blue", "default.png");
        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);

        // when
        Member member = new Member("haha", "haha@example.com", profileImage, feedbackPreferences);

        // then
        assertThat(member.getName()).isEqualTo("haha");
        assertThat(member.getEmail()).isEqualTo("haha@example.com");
        assertThat(member.getProfileImage().getBackgroundColor()).isEqualTo("blue");
        assertThat(member.getProfileImage().getImage()).isEqualTo("default.png");
    }

    @Test
    @DisplayName("프로필 이미지 변경이 정상적으로 이루어지는지 테스트")
    void changeProfile_프로필변경() {
        // given
        ProfileImage oldProfile = new ProfileImage("blue", "default.png");
        ProfileImage newProfile = new ProfileImage("red", "new.png");
        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        Member member = new Member("haha", "haha@example.com", oldProfile, feedbackPreferences);

        // when
        member.changeProfile(newProfile);

        // then
        assertThat(member.getProfileImage().getBackgroundColor()).isEqualTo("red");
        assertThat(member.getProfileImage().getImage()).isEqualTo("new.png");
    }

    @Test
    @DisplayName("회원 이름 변경이 정상적으로 이루어지는지 테스트")
    void changeName_이름변경() {
        // given
        ProfileImage profileImage = new ProfileImage("blue", "default.png");
        List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
        Member member = new Member("haha", "haha@example.com", profileImage, feedbackPreferences);

        // when
        member.changeName("hoho");

        // then
        assertThat(member.getName()).isEqualTo("hoho");
    }
}
