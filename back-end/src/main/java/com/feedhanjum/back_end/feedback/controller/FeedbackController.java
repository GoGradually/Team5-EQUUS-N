package com.feedhanjum.back_end.feedback.controller;

import com.feedhanjum.back_end.auth.infra.Login;
import com.feedhanjum.back_end.core.dto.Paged;
import com.feedhanjum.back_end.feedback.controller.dto.request.*;
import com.feedhanjum.back_end.feedback.controller.dto.response.FeedbackReportDto;
import com.feedhanjum.back_end.feedback.controller.dto.response.FrequentFeedbackRequestForApiResponse;
import com.feedhanjum.back_end.feedback.controller.dto.response.RegularFeedbackRequestForApiResponse;
import com.feedhanjum.back_end.feedback.domain.FeedbackReport;
import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.feedback.domain.ObjectiveFeedback;
import com.feedhanjum.back_end.feedback.exception.NoRegularFeedbackRequestException;
import com.feedhanjum.back_end.feedback.service.FeedbackQueryService;
import com.feedhanjum.back_end.feedback.service.FeedbackService;
import com.feedhanjum.back_end.feedback.service.dto.ReceivedFeedbackDto;
import com.feedhanjum.back_end.feedback.service.dto.SentFeedbackDto;
import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final FeedbackQueryService feedbackQueryService;

    @Operation(summary = "수시 피드백 전송", description = "팀별로 수시 피드백을 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "수시 피드백 전송 성공. 연관된 수시 피드백 요청도 함께 삭제", useReturnTypeSchema = true)
    })
    @PostMapping("/feedbacks/frequent")
    public ResponseEntity<Void> sendFrequentFeedback(@Login Long senderId,
                                                     @Valid @RequestBody FrequentFeedbackSendRequest request) {
        feedbackService.sendFrequentFeedback(senderId, request.receiverId(), request.teamId(),
                request.isAnonymous() ? FeedbackType.ANONYMOUS : FeedbackType.IDENTIFIED
                , request.feedbackFeeling(), request.objectiveFeedbacks(), request.subjectiveFeedback());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "정기 피드백 전송", description = "일정별로 정기 피드백을 전송합니다. 정기 피드백 요청을 통해 피드백 작성을 요청받았어야 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "정기 피드백 전송 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "선행되는 정기 피드백 요청이 없을 경우", useReturnTypeSchema = true)
    })
    @PostMapping("/feedbacks/regular")
    public ResponseEntity<Void> sendRegularFeedback(@Login Long senderId,
                                                    @Valid @RequestBody RegularFeedbackSendRequest request) {
        try {
            feedbackService.sendRegularFeedback(senderId, request.receiverId(), request.scheduleId(),
                    request.isAnonymous() ? FeedbackType.ANONYMOUS : FeedbackType.IDENTIFIED
                    , request.feedbackFeeling(), request.objectiveFeedbacks(), request.subjectiveFeedback());
        } catch (NoRegularFeedbackRequestException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "수시 피드백 요청", description = "수시 피드백을 요청합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "수시 피드백 요청 성공", useReturnTypeSchema = true)
    })
    @PostMapping("/feedbacks/frequent/request")
    public ResponseEntity<Void> requestFrequentFeedback(@Login Long senderId,
                                                        @Valid @RequestBody FrequentFeedbackRequestForApiRequest request) {
        feedbackService.requestFrequentFeedback(senderId, request.teamId(),
                request.receiverId(), request.requestedContent());
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "수시 피드백 요청 조회", description = "팀별 수시 피드백 요청을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀별 수시 피드백 요청 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/feedbacks/frequent/request")
    public ResponseEntity<List<FrequentFeedbackRequestForApiResponse>> getFrequentFeedbackRequest(@Login Long receiverId,
                                                                                                  @ParameterObject @Valid FrequentFeedbackRequestQueryRequest request) {
        List<FrequentFeedbackRequestForApiResponse> frequentFeedbackRequests = feedbackQueryService.getFrequentFeedbackRequests(receiverId, request.teamId());
        return ResponseEntity.ok(frequentFeedbackRequests);
    }

    @Operation(summary = "정기 피드백 요청 조회", description = "일정별 정기 피드백 요청을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정별 정기 피드백 요청 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/feedbacks/regular/request")
    public ResponseEntity<List<RegularFeedbackRequestForApiResponse>> getRegularFeedbackRequest(@Login Long receiverId,
                                                                                                @ParameterObject @Valid RegularFeedbackRequestQueryRequest request) {
        List<RegularFeedbackRequestForApiResponse> regularFeedbackRequests = feedbackQueryService.getRegularFeedbackRequests(receiverId, request.scheduleId());
        return ResponseEntity.ok(regularFeedbackRequests);
    }

    @Operation(summary = "피드백 좋아요", description = "피드백에 좋아요를 누릅니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "피드백 좋아요 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "403", description = "본인이 아닌 경우")
    })
    @PostMapping("/member/{memberId}/feedbacks/{feedbackId}/liked")
    public ResponseEntity<Void> likeFeedback(@Login Long loginId, @PathVariable Long memberId, @PathVariable Long feedbackId) {
        if (!Objects.equals(loginId, memberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        feedbackService.likeFeedback(feedbackId, memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "피드백 좋아요 취소", description = "피드백에 누른 좋아요를 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "피드백 좋아요 취소 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "403", description = "본인이 아닌 경우")
    })
    @DeleteMapping("/member/{memberId}/feedbacks/{feedbackId}/liked")
    public ResponseEntity<Void> unlikeFeedback(@Login Long loginId, @PathVariable Long memberId, @PathVariable Long feedbackId) {
        if (!Objects.equals(loginId, memberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        feedbackService.unlikeFeedback(feedbackId, memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "정기 피드백 건너뛰기", description = "해당 일정 정기 피드백을 건너뛰기합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "건너뛰기 성공", useReturnTypeSchema = true)
    })
    @DeleteMapping("/feedbacks/regular/request")
    public ResponseEntity<Void> skipRegularFeedbackRequest(@Login Long receiverId,
                                                           @ParameterObject @Valid RegularFeedbackRequestQueryRequest request) {
        feedbackService.skipRegularFeedback(request.scheduleId(), receiverId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "보낸 피드백 조회하기", description = "받은 피드백을 조회힙니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "보낸 피드백 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "403", description = "본인이 아닌 경우", content = @Content)
    })
    @GetMapping("/feedbacks/sender/{senderId}")
    public ResponseEntity<Paged<SentFeedbackDto>> getSentFeedbacks(@Login Long loginId,
                                                                   @PathVariable Long senderId,
                                                                   @ParameterObject @Valid SentFeedbacksQueryRequest request) {
        if (!Objects.equals(loginId, senderId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Page<SentFeedbackDto> sentFeedbacks = feedbackQueryService.getSentFeedbacks(senderId, request.teamId(), request.filterHelpful(), request.page(), request.sortOrder());
        return ResponseEntity.ok(Paged.from(sentFeedbacks));
    }

    @Operation(summary = "받은 피드백 조회하기", description = "받은 피드백을 조회힙니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "보낸 피드백 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "403", description = "본인이 아닌 경우", content = @Content)
    })
    @GetMapping("/feedbacks/receiver/{receiverId}")
    public ResponseEntity<Paged<ReceivedFeedbackDto>> getReceivedFeedbacks(@Login Long loginId,
                                                                           @PathVariable Long receiverId,
                                                                           @ParameterObject @Valid ReceivedFeedbacksQueryRequest request) {
        if (!Objects.equals(loginId, receiverId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Page<ReceivedFeedbackDto> receivedFeedbacks = feedbackQueryService.getReceivedFeedbacks(receiverId, request.teamId(), request.filterHelpful(), request.page(), request.sortOrder());
        return ResponseEntity.ok(Paged.from(receivedFeedbacks));
    }

    @Operation(summary = "피드백 선호도 선택지 조회", description = "사용자에게 피드백 선호도 선택지를 제공하기 위한 API")
    @ApiResponse(responseCode = "200", description = "선택 가능한 피드백 선호 정보를 반환한다.")
    @GetMapping("/feedback/preference")
    public ResponseEntity<Map<String, List<String>>> getSelectableFeedbackPreference() {
        Map<String, List<String>> feedbackPreferenceMap = new HashMap<>();
        for (FeedbackPreference feedbackPreference : FeedbackPreference.values()) {
            List<String> descriptions = feedbackPreferenceMap.computeIfAbsent(feedbackPreference.getType(), key -> new ArrayList<>());
            descriptions.add(feedbackPreference.getDescription());
        }
        return ResponseEntity.ok(feedbackPreferenceMap);
    }

    @Operation(summary = "객관식 피드백 선택지 조회", description = "사용자에게 객관식 피드백 선택지를 제공하기 위한 API")
    @ApiResponse(responseCode = "200", description = "선택 가능한 객관식 피드백 정보를 반환한다.")
    @GetMapping("/feedback/objective")
    public ResponseEntity<Map<String, Map<String, List<String>>>> getSelectableObjectFeedbacks() {
        Map<String, Map<String, List<String>>> objectiveFeedbacksMap = new HashMap<>();
        for (ObjectiveFeedback objectiveFeedback : ObjectiveFeedback.values()) {
            Map<String, List<String>> objectiveFeedbackFeelingMap = objectiveFeedbacksMap.computeIfAbsent(objectiveFeedback.getFeeling().getDescription(), key -> new HashMap<>());
            List<String> objectiveFeedbackDescriptions = objectiveFeedbackFeelingMap.computeIfAbsent(objectiveFeedback.getCategory().getDescription(), key -> new ArrayList<>());
            objectiveFeedbackDescriptions.add(objectiveFeedback.getDescription());
        }
        return ResponseEntity.ok(objectiveFeedbacksMap);
    }

    @Operation(summary = "피드백 리포트 조회", description = "로그인 유저의 피드백 리포트를 조회힙니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드백 리포트 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/feedbacks/report")
    public ResponseEntity<FeedbackReportDto> getFeedbackReport(@Login Long receiverId) {
        FeedbackReport feedbackReport = feedbackQueryService.getFeedbackReport(receiverId);
        return ResponseEntity.ok(FeedbackReportDto.from(feedbackReport));
    }
}
