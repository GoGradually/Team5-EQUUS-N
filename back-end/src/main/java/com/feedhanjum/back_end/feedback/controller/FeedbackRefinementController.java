package com.feedhanjum.back_end.feedback.controller;

import com.feedhanjum.back_end.auth.infra.Login;
import com.feedhanjum.back_end.feedback.controller.dto.request.FeedbackRefineRequest;
import com.feedhanjum.back_end.feedback.controller.dto.response.RefineRemainCountResponse;
import com.feedhanjum.back_end.feedback.service.FeedbackRefineService;
import com.feedhanjum.back_end.feedback.service.dto.FeedbackRefineDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/feedback-refinement")
@RequiredArgsConstructor
public class FeedbackRefinementController {

    private final FeedbackRefineService feedbackRefineService;

    @Operation(summary = "주관식 피드백 다듬기", description = "AI를 활용해 주관식 피드백을 다듬습니다. 호출 가능 횟수 제한은 3회입니다. 피드백 작성 시 호출 가능 횟수 제한이 초기화됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주관식 피드백 다듬기 성공", content = @Content(schema = @Schema(implementation = FeedbackRefineDto.class))),
            @ApiResponse(responseCode = "429", description = "호출 가능 횟수 초과", content = @Content),
            @ApiResponse(responseCode = "502", description = "외부 API 서버 오류", content = @Content)
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ResponseEntity<FeedbackRefineDto>> refineFeedback(@Login Long callerId, @Valid @RequestBody FeedbackRefineRequest request) {
        return feedbackRefineService.refineFeedback(callerId, request.receiverId(), request.subjectiveFeedback())
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "남은 호출 가능 횟수 조회", description = "주관식 피드백 다듬기 호출 가능 횟수를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "남은 호출 가능 횟수 조회 성공")
    })
    @GetMapping(value = "/remain-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RefineRemainCountResponse> getRemainCount(@Login Long callerId) {
        return ResponseEntity.ok(new RefineRemainCountResponse(feedbackRefineService.getRefineCount(callerId)));
    }
}
