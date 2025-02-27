package com.feedhanjum.back_end.auth.controller;

import com.feedhanjum.back_end.auth.controller.dto.request.*;
import com.feedhanjum.back_end.auth.controller.dto.response.*;
import com.feedhanjum.back_end.auth.controller.mapper.MemberMapper;
import com.feedhanjum.back_end.auth.domain.EmailSignupToken;
import com.feedhanjum.back_end.auth.domain.GoogleSignupToken;
import com.feedhanjum.back_end.auth.domain.MemberDetails;
import com.feedhanjum.back_end.auth.domain.PasswordResetToken;
import com.feedhanjum.back_end.auth.exception.PasswordResetTokenVerifyRequiredException;
import com.feedhanjum.back_end.auth.exception.SignupTokenVerifyRequiredException;
import com.feedhanjum.back_end.auth.infra.SessionConst;
import com.feedhanjum.back_end.auth.service.AuthService;
import com.feedhanjum.back_end.auth.service.dto.GoogleLoginResultDto;
import com.feedhanjum.back_end.member.domain.ProfileImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MemberMapper memberMapper;

    /**
     * 회원가입을 처리하는 핸들러
     *
     * @param request
     * @return 회원 가입 성공 여부
     */
    @Operation(
            summary = "회원가입",
            description = "회원가입을 처리합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입을 처리합니다. 동시에 로그인 처리도 수행합니다."),
            @ApiResponse(responseCode = "401", description = "이메일이 검증되지 않았을 경우", content = @Content),
            @ApiResponse(responseCode = "409", description = "중복된 이메일이 있을 경우", content = @Content)
    })
    @PostMapping("/email/signup")
    public ResponseEntity<MemberSignupResponse> signup(
            HttpSession session,
            @Valid @RequestBody MemberSignupRequest request) {
        Object emailObject = session.getAttribute(SessionConst.SIGNUP_TOKEN_VERIFIED_EMAIL);
        if (!(emailObject instanceof String email) || !email.equals(request.email())) {
            throw new SignupTokenVerifyRequiredException();
        }
        MemberDetails member = memberMapper.toEntity(request);
        String name = request.name();
        ProfileImage profileImage = request.profileImage();

        MemberDetails savedMember = authService.registerEmail(member, name, profileImage, request.feedbackPreferences());

        session.removeAttribute(SessionConst.SIGNUP_TOKEN_VERIFIED_EMAIL);
        MemberSignupResponse response = memberMapper.toResponse(savedMember);

        session.setAttribute(SessionConst.MEMBER_ID, savedMember.getId());
        log.info("email signup success {}", savedMember.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "로그인",
            description = "로그인을 처리합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인을 처리합니다. 세션 ID를 쿠키로 등록합니다."),
            @ApiResponse(responseCode = "401", description = "이메일 또는 비밀번호가 올바르지 않습니다.", content = @Content)
    })
    @PostMapping("/email/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        MemberDetails member = authService.authenticateEmail(request.email(), request.password());

        session.setAttribute(SessionConst.MEMBER_ID, member.getId());

        LoginResponse response = new LoginResponse("로그인에 성공했습니다.", member.getId(), member.getEmail());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃을 처리합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그아웃 처리 성공")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원가입 이메일 중복검증 & 토큰 발송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 검증 토큰 발송 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "409", description = "이메일 중복", content = @Content),
            @ApiResponse(responseCode = "429", description = "이메일 발송 지연으로 인해 실패", content = @Content)
    })
    @PostMapping(value = "/send-signup-verification-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignupEmailSendResponse> sendSignupVerificationEmail(@Valid @RequestBody SignupEmailSendRequest request) {
        EmailSignupToken emailSignupToken = authService.sendSignupVerificationEmail(request.email());
        SignupEmailSendResponse signupEmailSendResponse = new SignupEmailSendResponse(emailSignupToken.getExpireDate());
        return ResponseEntity.ok(signupEmailSendResponse);
    }

    @Operation(summary = "회원가입 이메일 토큰 인증")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "이메일 토큰 인증 성공. 세션에 해당 내역 저장"),
            @ApiResponse(responseCode = "400", description = "이메일 토큰 인증 실패", content = @Content)
    })
    @PostMapping("/verify-signup-email-token")
    public ResponseEntity<Void> verifySignupEmailToken(HttpSession session, @Valid @RequestBody SignupEmailVerifyRequest request) {
        authService.validateSignupToken(request.email(), request.code());
        session.setAttribute(SessionConst.SIGNUP_TOKEN_VERIFIED_EMAIL, request.email());
        log.info("email signup token verification success. email: {}, token: {}", request.email(), request.code());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "비밀번호 초기화 이메일 토큰 발송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 초기화 토큰 발송 성공. 이메일이 존재하지 않아도 보안상 발송 성공처리"),
            @ApiResponse(responseCode = "429", description = "이메일 발송 지연으로 인해 실패", content = @Content)

    })
    @PostMapping(value = "/send-password-reset-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PasswordResetEmailSendResponse> sendPasswordResetEmail(
            @Valid @RequestBody PasswordResetEmailSendRequest request) {
        Optional<PasswordResetToken> passwordResetTokenOptional = authService.sendPasswordResetEmail(request.email());
        PasswordResetEmailSendResponse passwordResetEmailSendResponse;
        if (passwordResetTokenOptional.isEmpty()) {
            passwordResetEmailSendResponse = new PasswordResetEmailSendResponse(LocalDateTime.now().plusMinutes(PasswordResetToken.EXPIRE_MINUTE));
        } else {
            PasswordResetToken passwordResetToken = passwordResetTokenOptional.get();
            passwordResetEmailSendResponse = new PasswordResetEmailSendResponse(passwordResetToken.getExpireDate());
        }
        return ResponseEntity.ok(passwordResetEmailSendResponse);
    }

    @Operation(summary = "비밀번호 초기화 이메일 토큰 인증")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "비밀번호 초기화 토큰 인증 성공. 세션에 해당 내역 저장"),
            @ApiResponse(responseCode = "400", description = "비밀번호 초기화 토큰 인증 실패", content = @Content)
    })
    @PostMapping("/verify-password-reset-token")
    public ResponseEntity<Void> verifyPasswordResetToken(HttpSession session, @Valid @RequestBody PasswordResetEmailVerifyRequest request) {
        authService.validatePasswordResetToken(request.email(), request.code());
        session.setAttribute(SessionConst.PASSWORD_RESET_TOKEN_VERIFIED_EMAIL, request.email());
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "비밀번호 초기화", description = "비밀번호를 초기화한다. 미리 비밀번호 초기화 이메일 토큰 인증이 완료되었어야 한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "비밀번호 초기화 성공"),
            @ApiResponse(responseCode = "401", description = "비밀번호 검증 토큰 인증 필요", content = @Content)
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(HttpSession session,
                                              @Valid @RequestBody PasswordResetRequest request
    ) {
        Object emailObject = session.getAttribute(SessionConst.PASSWORD_RESET_TOKEN_VERIFIED_EMAIL);
        if (!(emailObject instanceof String email) || !email.equals(request.email())) {
            throw new PasswordResetTokenVerifyRequiredException();
        }
        authService.resetPassword(request.email(), request.newPassword());
        session.removeAttribute(SessionConst.PASSWORD_RESET_TOKEN_VERIFIED_EMAIL);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "구글 로그인 페이지 url 조회", description = "사용자가 구글 로그인을 하게 될 페이지 주소를 조회한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/google/login-url")
    public ResponseEntity<GoogleLoginUrlResponse> getGoogleLoginUrl(@RequestHeader(HttpHeaders.REFERER) String referer) {
        String redirectBaseUrl = getBaseUrl(referer);

        return ResponseEntity.ok(new GoogleLoginUrlResponse(authService.getGoogleLoginUrl(redirectBaseUrl)));
    }

    @Operation(summary = "구글 로그인 or 구글 회원가입 토큰 응답", description = "구글 회원가입된 상태라면 로그인, 아니라면 회원가입 시 필요한 토큰을 발급합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 or 회원가입 토큰 발급 성공"),
            @ApiResponse(responseCode = "401", description = "구글 로그인 실패", content = @Content),
            @ApiResponse(responseCode = "409", description = "이미 중복되는 이메일 계정이 존재하는 경우", content = @Content)
    })
    @PostMapping("/google/login")
    public ResponseEntity<GoogleLoginResponse> loginWithGoogle(HttpSession session, @RequestHeader(HttpHeaders.REFERER) String referer, @Valid @RequestBody GoogleLoginRequest request) {
        GoogleLoginResultDto loginResult = authService.authenticateGoogle(request.code(), getBaseUrl(referer));

        if (loginResult.isAuthenticated()) {
            MemberDetails member = loginResult.memberDetails();
            LoginResponse response = new LoginResponse("로그인에 성공했습니다.", member.getId(), member.getEmail());
            session.setAttribute(SessionConst.MEMBER_ID, member.getId());
            log.info("member {} directly login google account", member.getId());
            return ResponseEntity.ok(GoogleLoginResponse.authenticated(response));
        }

        GoogleSignupToken signupToken = loginResult.googleSignupToken();
        log.info("generate google signup token with email {} and code: {}", signupToken.getEmail(), signupToken.getCode());
        return ResponseEntity.ok(GoogleLoginResponse.signupRequired(signupToken));
    }


    @Operation(summary = "구글 회원가입", description = "구글 계정으로 회원가입 후 로그인합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "구글로 회원가입 성공 및 로그인 성공"),
            @ApiResponse(responseCode = "400", description = "구글 회원가입 토큰 인증 실패", content = @Content),
            @ApiResponse(responseCode = "401", description = "구글 회원가입 실패", content = @Content)
    })
    @PostMapping("/google/signup")
    public ResponseEntity<MemberSignupResponse> signupWithGoogle(HttpSession session, @Valid @RequestBody GoogleSignupRequest request) {

        MemberDetails member = authService.registerGoogle(request.token(), request.profileImage(), request.feedbackPreferences());

        MemberSignupResponse response = memberMapper.toResponse(member);

        session.setAttribute(SessionConst.MEMBER_ID, member.getId());
        log.info("member {} signup and login with google account", member.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private String getBaseUrl(String referer) {
        try {
            URL url = new URL(referer);
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            if (url.getPort() != -1) {
                baseUrl += ":" + url.getPort();
            }
            return baseUrl;

        } catch (MalformedURLException e) {
            throw new RuntimeException("referrer header is not valid url", e);
        }
    }
}