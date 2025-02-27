package com.feedhanjum.back_end.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FeedbackPreferenceTest {

    @Test
    @DisplayName("완곡한 Description으로 FeedbackPreference를 반환")
    void fromDescription_완곡한() {
        // given
        String description = "완곡한";

        // when
        FeedbackPreference result = FeedbackPreference.fromDescription(description);

        // then
        assertThat(result).isEqualTo(FeedbackPreference.EUPHEMISTIC);
    }

    @Test
    @DisplayName("솔직한 Description으로 FeedbackPreference를 반환")
    void fromDescription_솔직한() {
        // given
        String description = "솔직한";

        // when
        FeedbackPreference result = FeedbackPreference.fromDescription(description);

        // then
        assertThat(result).isEqualTo(FeedbackPreference.HONEST);
    }

    @Test
    @DisplayName("구체적인 Description으로 FeedbackPreference를 반환")
    void fromDescription_구체적인() {
        // given
        String description = "구체적인";

        // when
        FeedbackPreference result = FeedbackPreference.fromDescription(description);

        // then
        assertThat(result).isEqualTo(FeedbackPreference.DETAILED);
    }

    @Test
    @DisplayName("유효하지 않은 Description으로 예외 발생")
    void fromDescription_유효하지않은설명() {
        // given
        String description = "invalidDescription";

        // when // then
        assertThatThrownBy(() -> FeedbackPreference.fromDescription(description))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Description이 null일 때 IllegalArgumentException 발생")
    void fromDescription_null설명() {
        // given
        String description = null;

        // when // then
        assertThatThrownBy(() -> FeedbackPreference.fromDescription(description))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 Description으로 예외를 반환")
    void fromDescription_빈설명() {
        // given
        String description = "";

        // when // then
        assertThatThrownBy(() -> FeedbackPreference.fromDescription(description))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Style Preference가 없을 경우 0을 반환")
    void countStylePreference_비어있음() {
        // given
        List<FeedbackPreference> preferences = Collections.emptyList();

        // when
        int count = FeedbackPreference.countStylePreference(preferences);

        // then
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("Style Preference만 있을 경우 Style 카운트 반환")
    void countStylePreference_스타일만있을때() {
        // given
        List<FeedbackPreference> preferences = Arrays.asList(
                FeedbackPreference.EUPHEMISTIC,
                FeedbackPreference.HONEST,
                FeedbackPreference.LIGHT
        );

        // when
        int count = FeedbackPreference.countStylePreference(preferences);

        // then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("Content Preference만 있을 때 올바르게 카운트 반환")
    void 내용선호카운트_컨텐츠선호만() {
        // given
        List<FeedbackPreference> preferences = Arrays.asList(
                FeedbackPreference.REALISTIC,
                FeedbackPreference.LOGICAL,
                FeedbackPreference.CLEAR
        );

        // when
        int count = FeedbackPreference.countContentPreference(preferences);

        // then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("Style Preference만 있을 때 0 반환")
    void 내용선호카운트_스타일선호만() {
        // given
        List<FeedbackPreference> preferences = Arrays.asList(
                FeedbackPreference.EUPHEMISTIC,
                FeedbackPreference.HONEST,
                FeedbackPreference.LIGHT
        );

        // when
        int count = FeedbackPreference.countContentPreference(preferences);

        // then
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("Content와 Style Preference 혼합 시 Content만 카운트 반환")
    void 내용선호카운트_혼합된선호() {
        // given
        List<FeedbackPreference> preferences = Arrays.asList(
                FeedbackPreference.EUPHEMISTIC,
                FeedbackPreference.REALISTIC,
                FeedbackPreference.LIGHT,
                FeedbackPreference.LOGICAL
        );

        // when
        int count = FeedbackPreference.countContentPreference(preferences);

        // then
        assertThat(count).isEqualTo(2);
    }

}