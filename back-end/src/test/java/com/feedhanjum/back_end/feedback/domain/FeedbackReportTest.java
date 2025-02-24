package com.feedhanjum.back_end.feedback.domain;

import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.test.util.DomainTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.feedhanjum.back_end.feedback.domain.ObjectiveFeedback.*;
import static org.assertj.core.api.Assertions.assertThat;

class FeedbackReportTest {


    Member sender = DomainTestUtils.createMemberWithId("sender");
    Member receiver = DomainTestUtils.createMemberWithId("receiver");
    Team team = DomainTestUtils.createTeamWithId("team", sender);

    private Feedback createFeedback(ObjectiveFeedback objectiveFeedback) {
        return Feedback.builder()
                .sender(sender)
                .team(team)
                .receiver(receiver)
                .subjectiveFeedback("안녕")
                .feedbackFeeling(objectiveFeedback.getFeeling())
                .objectiveFeedbacks(List.of(objectiveFeedback))
                .build();
    }

    @Test
    @DisplayName("같은 카테고리,다른 필링에 속한 피드백")
    void test1() {
        // given

        List<Feedback> feedbacks = List.of(
                createFeedback(LOGICAL),
                createFeedback(LOGICAL),
                createFeedback(LOGICAL),
                createFeedback(LOGICAL),
                createFeedback(LOGICAL),
                createFeedback(LOGICAL),

                createFeedback(CONCISE_REQUIRED),
                createFeedback(CONCISE_REQUIRED),
                createFeedback(CONCISE_REQUIRED),
                createFeedback(CONCISE_REQUIRED)
        );

        // when
        FeedbackReport report = FeedbackReport.fromFeedbacks(feedbacks);

        // then
        assertThat(report.getFeedbackCount()).isEqualTo(10);
        List<FeedbackReport.CategoryCount> overviews = report.getOverviews();
        assertThat(overviews).filteredOn(o -> o.getCategory().equals(FeedbackCategory.EFFORT))
                .first().satisfies(overview -> {
                    assertThat(overview.getGoodCount()).isEqualTo(6);
                    assertThat(overview.getBadCount()).isEqualTo(-4);
                });
        List<FeedbackReport.KeywordCount> allKeywords = report.getAllKeywords();
        assertThat(allKeywords).hasSize(2);
        assertThat(allKeywords).filteredOn(keyword -> keyword.getKeyword().equals(LOGICAL)).first().satisfies(keyword -> {
            assertThat(keyword.getCount()).isEqualTo(6);
        });
        assertThat(allKeywords).filteredOn(keyword -> keyword.getKeyword().equals(CONCISE_REQUIRED)).first().satisfies(keyword -> {
            assertThat(keyword.getCount()).isEqualTo(-4);
        });
    }

    @Test
    @DisplayName("피드백 개수가 10개 미만이면 조회 시 null")
    void test2() {
        // given
        List<Feedback> feedbacks = List.of(
                createFeedback(COLLABORATIVE),
                createFeedback(COLLABORATIVE),
                createFeedback(COLLABORATIVE),
                createFeedback(COLLABORATIVE),
                createFeedback(COLLABORATIVE),
                createFeedback(COLLABORATIVE),
                createFeedback(COLLABORATIVE),
                createFeedback(COLLABORATIVE),
                createFeedback(COLLABORATIVE)
        );

        // when
        FeedbackReport report = FeedbackReport.fromFeedbacks(feedbacks);

        // then
        assertThat(report.getFeedbackCount()).isEqualTo(9);
        assertThat(report.getTopKeywords()).isNull();
        assertThat(report.getOverviews()).isNull();
        assertThat(report.getAllKeywords()).isNull();
    }

    @Test
    @DisplayName("받은 피드백 개수가 같을 경우 '칭찬해요'가 '아쉬워요'보다 우선 정렬")
    void test3() {
        // given
        List<Feedback> feedbacks = List.of(
                createFeedback(HARDWORKING),
                createFeedback(HARDWORKING),

                createFeedback(DETAILED_REQUIRED),
                createFeedback(DETAILED_REQUIRED),
                createFeedback(DETAILED_REQUIRED),
                createFeedback(DETAILED_REQUIRED),

                createFeedback(COLLABORATIVE),
                createFeedback(COLLABORATIVE),
                createFeedback(COLLABORATIVE),
                createFeedback(COLLABORATIVE)
        );

        // when
        FeedbackReport report = FeedbackReport.fromFeedbacks(feedbacks);

        // then
        assertThat(report.getAllKeywords())
                .satisfies(allKeywords -> {
                    assertThat(allKeywords.get(0).getKeyword()).isEqualTo(COLLABORATIVE);
                    assertThat(allKeywords.get(1).getKeyword()).isEqualTo(DETAILED_REQUIRED);
                    assertThat(allKeywords.get(2).getKeyword()).isEqualTo(HARDWORKING);
                });
    }

    @Test
    @DisplayName("all keywords에서 '칭찬해요','아쉬워요'별로 많이 받은게 topKeywords")
    void test4() {
        // given
        List<Feedback> feedbacks = List.of(
                createFeedback(LOGICAL),
                createFeedback(LOGICAL),
                createFeedback(LOGICAL),

                createFeedback(POLITE_REQUIRED),
                createFeedback(POLITE_REQUIRED),
                createFeedback(POLITE_REQUIRED),
                createFeedback(POLITE_REQUIRED),

                createFeedback(DETAILED_REQUIRED),
                createFeedback(DETAILED_REQUIRED),

                createFeedback(RESPONSIBLE_REQUIRED),

                createFeedback(THOROUGH_REQUIRED)
        );


        // when
        FeedbackReport report = FeedbackReport.fromFeedbacks(feedbacks);

        assertThat(report.getTopKeywords()).satisfies(topKeywords -> {
            assertThat(topKeywords).hasSize(2);
            assertThat(topKeywords).filteredOn(keyword -> keyword.getFeeling()
                            .equals(FeedbackFeeling.POSITIVE)).first()
                    .matches(keyword -> keyword.getKeyword().equals(LOGICAL));

            assertThat(topKeywords).filteredOn(keyword -> keyword.getFeeling()
                            .equals(FeedbackFeeling.CONSTRUCTIVE)).first()
                    .matches(keyword -> keyword.getKeyword().equals(POLITE_REQUIRED));
        });

    }


}