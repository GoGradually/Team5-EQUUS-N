package com.feedhanjum.back_end.member.controller;

import com.feedhanjum.back_end.auth.infra.Login;
import com.feedhanjum.back_end.feedback.service.FeedbackService;
import com.feedhanjum.back_end.member.controller.dto.LoginMemberResponse;
import com.feedhanjum.back_end.member.controller.dto.MemberFeedbackPreferenceResponse;
import com.feedhanjum.back_end.member.controller.dto.MemberResponse;
import com.feedhanjum.back_end.member.controller.dto.ProfileChangeRequest;
import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import com.feedhanjum.back_end.member.domain.ProfileImage;
import com.feedhanjum.back_end.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;
    private final FeedbackService feedbackService;

    @Operation(summary = "특정 회원 정보 조회", description = "특정 회원의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보 조회에 성공했을 경우, 해당 회원 정보를 반환합니다."),
            @ApiResponse(responseCode = "404", description = "해당 회원이 존재하지 않을 경우", content = @Content)
    })
    @GetMapping("/member/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        MemberResponse memberResponse = new MemberResponse(memberService.getMemberById(id));
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    @Operation(summary = "특정 회원 정보 조회", description = "특정 회원의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보 조회에 성공했을 경우, 해당 회원 정보를 반환합니다."),
            @ApiResponse(responseCode = "404", description = "해당 회원이 존재하지 않을 경우", content = @Content)
    })
    @GetMapping("/member")
    public ResponseEntity<LoginMemberResponse> getLoginMember(@Login Long id) {
        LoginMemberResponse loginMemberResponse = new LoginMemberResponse(
                memberService.getMemberById(id),
                feedbackService.getReceivedFeedbackCount(id),
                feedbackService.getSentFeedbackCount(id)
        );
        return new ResponseEntity<>(loginMemberResponse, HttpStatus.OK);
    }

    @Operation(summary = "회원의 정보를 변경한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "변경된 회원 정보를 반환한다. " +
                    "세션을 통해 받은 회원의 정보를 수정하기 때문에, 다른 회원의 정보를 수정할 수 없다."),
            @ApiResponse(responseCode = "400", description = "변경할 회원 정보 폼이 잘못됐을 경우, 주로 이름이 제한 범위 밖인 경우", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 회원이 존재하지 않을 경우", content = @Content)
    })
    @PostMapping("/member")
    public ResponseEntity<MemberResponse> changeProfile(@Login Long memberId, @Valid @RequestBody ProfileChangeRequest profileChangeRequest) {
        String name = profileChangeRequest.name();
        ProfileImage profileImage = profileChangeRequest.profileImage();
        MemberResponse memberResponse = new MemberResponse(memberService.changeProfile(memberId, name, profileImage));
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }


    @Operation(summary = "피드백 선호 정보 조회", description = "회원의 피드백 선호 정보를 반환한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원의 피드백 선호 정보를 반환한다."),
            @ApiResponse(responseCode = "404", description = "해당 회원이 존재하지 않을 경우", content = @Content)
    })
    @GetMapping("/member/feedback-prefer")
    public ResponseEntity<MemberFeedbackPreferenceResponse> getFeedbackPreference(Long findMemberId) {
        MemberFeedbackPreferenceResponse memberDto = new MemberFeedbackPreferenceResponse(memberService.getMemberFeedbackPreference(findMemberId));
        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }

    @Operation(summary = "피드백 선호 정보 변경", description = "회원의 피드백 선호 정보를 변경한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "변경된 회원 정보를 반환한다. " +
                    "세션을 통해 받은 회원의 정보를 수정하기 때문에, 다른 회원의 정보를 수정할 수 없다."),
            @ApiResponse(responseCode = "400", description = "변경할 피드백 선호도 정보가 잘못됐을 경우", content = @Content),
            @ApiResponse(responseCode = "404", description = "해당 회원이 존재하지 않을 경우", content = @Content)
    })
    @PostMapping("/member/feedback-prefer")
    public ResponseEntity<MemberFeedbackPreferenceResponse> changeFeedbackPreference(@Login Long memberId, @Valid @RequestBody List<FeedbackPreference> feedbackPreferences) {
        MemberFeedbackPreferenceResponse memberDto = new MemberFeedbackPreferenceResponse(memberService.changeFeedbackPreference(memberId, feedbackPreferences));
        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }
}
