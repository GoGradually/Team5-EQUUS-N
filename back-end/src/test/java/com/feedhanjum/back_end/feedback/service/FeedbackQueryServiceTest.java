package com.feedhanjum.back_end.feedback.service;

import com.feedhanjum.back_end.feedback.domain.Feedback;
import com.feedhanjum.back_end.feedback.repository.FeedbackQueryRepository;
import com.feedhanjum.back_end.feedback.repository.FrequentFeedbackRequestQueryRepository;
import com.feedhanjum.back_end.feedback.service.dto.ReceivedFeedbackDto;
import com.feedhanjum.back_end.feedback.service.dto.SentFeedbackDto;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.team.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackQueryServiceTest {
    @Mock
    private FeedbackQueryRepository feedbackQueryRepository;
    @Mock
    private FrequentFeedbackRequestQueryRepository feedbackRequestQueryRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TeamRepository teamRepository;
    @InjectMocks
    private FeedbackQueryService feedbackQueryService;

    @Nested
    @DisplayName("getReceivedFeedbacks 메소드 테스트")
    class GetReceivedFeedbacksTest {
        @Test
        @DisplayName("받은 피드백 조회 성공")
        void test1() {
            // given
            Long receiverId = 1L;
            Long teamId = 2L;
            boolean filterHelpful = true;
            int page = 0;
            Sort.Direction sortOrder = Sort.Direction.ASC;
            Page<Feedback> feedbacks = mock();
            Page<ReceivedFeedbackDto> receivedFeedbacks = mock();

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.captor();

            when(memberRepository.findById(receiverId)).thenReturn(Optional.of(mock()));
            when(teamRepository.findById(teamId)).thenReturn(Optional.of(mock()));

            when(feedbackQueryRepository.findReceivedFeedbacks(eq(receiverId), eq(teamId), eq(filterHelpful), pageableCaptor.capture(), eq(sortOrder))).thenReturn(feedbacks);
            when(feedbacks.map(any(Function.class))).thenReturn(receivedFeedbacks);

            // when
            Page<ReceivedFeedbackDto> result = feedbackQueryService.getReceivedFeedbacks(receiverId, teamId, filterHelpful, page, sortOrder);

            // then
            assertThat(result).isEqualTo(receivedFeedbacks);
            Pageable pageable = pageableCaptor.getValue();
            assertThat(pageable.getPageNumber()).isEqualTo(page);
            assertThat(pageable.getPageSize()).isEqualTo(10);
        }

        @Test
        @DisplayName("page가 0 미만일 때 실패")
        void test2() {
            // given
            Long receiverId = 1L;
            Long teamId = 2L;
            boolean filterHelpful = true;
            int page = -1;
            Sort.Direction sortOrder = Sort.Direction.ASC;

            assertThatThrownBy(() -> feedbackQueryService.getReceivedFeedbacks(receiverId, teamId, filterHelpful, page, sortOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("receiver가 없을 때 실패")
        void test3() {
            // given
            Long receiverId = 1L;
            Long teamId = 2L;
            boolean filterHelpful = true;
            int page = 0;
            Sort.Direction sortOrder = Sort.Direction.ASC;

            when(memberRepository.findById(receiverId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> feedbackQueryService.getReceivedFeedbacks(receiverId, teamId, filterHelpful, page, sortOrder))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        @DisplayName("team이 없을 때 실패")
        void test4() {
            // given
            Long receiverId = 1L;
            Long teamId = 2L;
            boolean filterHelpful = true;
            int page = 0;
            Sort.Direction sortOrder = Sort.Direction.ASC;

            when(memberRepository.findById(receiverId)).thenReturn(Optional.of(mock()));
            when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> feedbackQueryService.getReceivedFeedbacks(receiverId, teamId, filterHelpful, page, sortOrder))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getSentFeedbacks 메소드 테스트")
    class GetSentFeedbacksTest {
        @Test
        @DisplayName("보낸 피드백 조회 성공")
        void test1() {
            // given
            Long senderId = 1L;
            Long teamId = 2L;
            boolean filterHelpful = true;
            int page = 0;
            Sort.Direction sortOrder = Sort.Direction.ASC;
            Page<Feedback> feedbacks = mock();
            Page<SentFeedbackDto> sentFeedbacks = mock();

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.captor();

            when(memberRepository.findById(senderId)).thenReturn(Optional.of(mock()));
            when(teamRepository.findById(teamId)).thenReturn(Optional.of(mock()));

            when(feedbackQueryRepository.findSentFeedbacks(eq(senderId), eq(teamId), eq(filterHelpful), pageableCaptor.capture(), eq(sortOrder))).thenReturn(feedbacks);
            when(feedbacks.map(any(Function.class))).thenReturn(sentFeedbacks);

            // when
            Page<SentFeedbackDto> result = feedbackQueryService.getSentFeedbacks(senderId, teamId, filterHelpful, page, sortOrder);

            // then
            assertThat(result).isEqualTo(sentFeedbacks);
            Pageable pageable = pageableCaptor.getValue();
            assertThat(pageable.getPageNumber()).isEqualTo(page);
            assertThat(pageable.getPageSize()).isEqualTo(10);
        }

        @Test
        @DisplayName("page가 0 미만일 때 실패")
        void test2() {
            // given
            Long senderId = 1L;
            Long teamId = 2L;
            boolean filterHelpful = true;
            int page = -1;
            Sort.Direction sortOrder = Sort.Direction.ASC;

            assertThatThrownBy(() -> feedbackQueryService.getSentFeedbacks(senderId, teamId, filterHelpful, page, sortOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("sender가 없을 때 실패")
        void test3() {
            // given
            Long senderId = 1L;
            Long teamId = 2L;
            boolean filterHelpful = true;
            int page = 0;
            Sort.Direction sortOrder = Sort.Direction.ASC;

            when(memberRepository.findById(senderId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> feedbackQueryService.getSentFeedbacks(senderId, teamId, filterHelpful, page, sortOrder))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        @DisplayName("team이 없을 때 실패")
        void test4() {
            // given
            Long senderId = 1L;
            Long teamId = 2L;
            boolean filterHelpful = true;
            int page = 0;
            Sort.Direction sortOrder = Sort.Direction.ASC;

            when(memberRepository.findById(senderId)).thenReturn(Optional.of(mock()));
            when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> feedbackQueryService.getSentFeedbacks(senderId, teamId, filterHelpful, page, sortOrder))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }
}