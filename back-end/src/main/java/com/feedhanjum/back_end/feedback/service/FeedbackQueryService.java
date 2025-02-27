package com.feedhanjum.back_end.feedback.service;


import com.feedhanjum.back_end.feedback.controller.dto.response.FrequentFeedbackRequestForApiResponse;
import com.feedhanjum.back_end.feedback.controller.dto.response.RegularFeedbackRequestForApiResponse;
import com.feedhanjum.back_end.feedback.domain.Feedback;
import com.feedhanjum.back_end.feedback.domain.FeedbackReport;
import com.feedhanjum.back_end.feedback.domain.FrequentFeedbackRequest;
import com.feedhanjum.back_end.feedback.domain.RegularFeedbackRequest;
import com.feedhanjum.back_end.feedback.repository.FeedbackQueryRepository;
import com.feedhanjum.back_end.feedback.repository.FrequentFeedbackRequestQueryRepository;
import com.feedhanjum.back_end.feedback.repository.RegularFeedbackRequestQueryRepository;
import com.feedhanjum.back_end.feedback.service.dto.ReceivedFeedbackDto;
import com.feedhanjum.back_end.feedback.service.dto.SentFeedbackDto;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.team.repository.TeamRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class FeedbackQueryService {
    private static final int PAGE_SIZE = 10;
    private final FeedbackQueryRepository feedbackQueryRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final FrequentFeedbackRequestQueryRepository frequentFeedbackRequestQueryRepository;
    private final RegularFeedbackRequestQueryRepository regularFeedbackRequestQueryRepository;

    /**
     * @throws EntityNotFoundException  receiver나 team이 없을 때
     * @throws IllegalArgumentException page가 0 미만일 때
     */
    @Transactional(readOnly = true)
    public Page<ReceivedFeedbackDto> getReceivedFeedbacks(Long receiverId, @Nullable Long teamId, boolean filterHelpful, int page, Sort.Direction sortOrder) {
        if (page < 0) {
            throw new IllegalArgumentException("page는 0 이상의 값을 가져야 합니다.");
        }

        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        memberRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("receiverId에 해당하는 Member가 없습니다."));
        if (teamId != null)
            teamRepository.findById(teamId)
                    .orElseThrow(() -> new EntityNotFoundException("teamId에 해당하는 Team이 없습니다."));


        Page<Feedback> receivedFeedbacks = feedbackQueryRepository.findReceivedFeedbacks(receiverId, teamId, filterHelpful, pageRequest, sortOrder);
        return receivedFeedbacks.map(ReceivedFeedbackDto::from);
    }

    /**
     * @throws EntityNotFoundException  sender나 team이 없을 때
     * @throws IllegalArgumentException page가 0 미만일 때
     */
    @Transactional(readOnly = true)
    public Page<SentFeedbackDto> getSentFeedbacks(Long senderId, @Nullable Long teamId, boolean filterHelpful, int page, Sort.Direction sortOrder) {
        if (page < 0) {
            throw new IllegalArgumentException("page는 0 이상의 값을 가져야 합니다.");
        }

        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        memberRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("senderId에 해당하는 Member가 없습니다."));
        if (teamId != null)
            teamRepository.findById(teamId)
                    .orElseThrow(() -> new EntityNotFoundException("teamId에 해당하는 Team이 없습니다."));


        Page<Feedback> receivedFeedbacks = feedbackQueryRepository.findSentFeedbacks(senderId, teamId, filterHelpful, pageRequest, sortOrder);
        return receivedFeedbacks.map(SentFeedbackDto::from);
    }

    @Transactional(readOnly = true)
    public List<FrequentFeedbackRequestForApiResponse> getFrequentFeedbackRequests(Long receiverId, Long teamId) {
        List<FrequentFeedbackRequest> requests = frequentFeedbackRequestQueryRepository.getFrequentFeedbackRequests(receiverId, teamId);
        return requests.stream().map(FrequentFeedbackRequestForApiResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<RegularFeedbackRequestForApiResponse> getRegularFeedbackRequests(Long receiverId, Long scheduleId) {
        List<RegularFeedbackRequest> requests = regularFeedbackRequestQueryRepository.getRegularFeedbackRequests(receiverId, scheduleId);
        return requests.stream().map(RegularFeedbackRequestForApiResponse::from).toList();
    }


    /**
     * @throws EntityNotFoundException when memberId does not exist
     */
    @Transactional(readOnly = true)
    public FeedbackReport getFeedbackReport(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("memberId에 해당하는 Member가 없습니다."));
        List<Feedback> receivedFeedback = feedbackQueryRepository.findReceivedFeedbacks(memberId);
        return FeedbackReport.fromFeedbacks(receivedFeedback);
    }
}
