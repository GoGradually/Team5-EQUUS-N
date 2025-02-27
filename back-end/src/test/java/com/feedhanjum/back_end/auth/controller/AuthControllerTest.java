package com.feedhanjum.back_end.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedhanjum.back_end.auth.controller.dto.request.LoginRequest;
import com.feedhanjum.back_end.auth.controller.dto.request.MemberSignupRequest;
import com.feedhanjum.back_end.auth.controller.dto.response.MemberSignupResponse;
import com.feedhanjum.back_end.auth.controller.mapper.MemberMapper;
import com.feedhanjum.back_end.auth.domain.MemberDetails;
import com.feedhanjum.back_end.auth.exception.EmailAlreadyExistsException;
import com.feedhanjum.back_end.auth.infra.SessionConst;
import com.feedhanjum.back_end.auth.service.AuthService;
import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new com.feedhanjum.back_end.auth.exception.AuthControllerAdvice())
                .build();
    }

    private MockHttpSession withSignupVerification(String email) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.SIGNUP_TOKEN_VERIFIED_EMAIL, email);
        return session;
    }

    @Nested
    @DisplayName("POST /api/auth/email/signup 테스트")
    class SignupTests {

        @Test
        @DisplayName("회원가입 성공 시 201(CREATED) 상태코드와 응답 반환")
        void signup_success() throws Exception {
            List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
            MemberSignupRequest request = new MemberSignupRequest("test@example.com", "abcd1234", "홍길동", null, feedbackPreferences);

            MemberDetails entity = MemberDetails.createEmailUser(null, request.email(), request.password());
            when(memberMapper.toEntity(any(MemberSignupRequest.class))).thenReturn(entity);

            MemberDetails savedMember = MemberDetails.createEmailUser(1L, "test@example.com", "abcd1234");
            when(authService.registerEmail(entity, request.name(), null, feedbackPreferences)).thenReturn(savedMember);

            MemberSignupResponse response = new MemberSignupResponse(1L, "test@example.com", "회원가입이 완료되었습니다.");
            when(memberMapper.toResponse(savedMember)).thenReturn(response);

            mockMvc.perform(post("/api/auth/email/signup")
                            .session(withSignupVerification(request.email()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.email").value("test@example.com"))
                    .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."));
        }

        @Test
        @DisplayName("이메일 중복 시 409(CONFLICT) 상태코드와 에러 메시지 반환")
        void signup_emailAlreadyExists() throws Exception {
            List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
            MemberSignupRequest request = new MemberSignupRequest("duplicate@example.com", "abcd1234", "홍길동", null, feedbackPreferences);

            MemberDetails entity = MemberDetails.createEmailUser(null, request.email(), request.password());
            when(memberMapper.toEntity(any(MemberSignupRequest.class))).thenReturn(entity);

            doThrow(new EmailAlreadyExistsException("이미 사용 중인 이메일입니다."))
                    .when(authService).registerEmail(entity, request.name(), null, feedbackPreferences);

            mockMvc.perform(post("/api/auth/email/signup")
                            .session(withSignupVerification(request.email()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.error").value("EMAIL_ALREADY_EXISTS"))
                    .andExpect(jsonPath("$.message").value("이미 사용 중인 이메일입니다."));
        }

        @Test
        @DisplayName("유효하지 않은 입력값일 경우 400(BAD_REQUEST) 상태코드 반환")
        void signup_invalidInput() throws Exception {
            List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
            MemberSignupRequest request = new MemberSignupRequest("", "12", "", null, feedbackPreferences);

            mockMvc.perform(post("/api/auth/email/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/auth/email/login 테스트")
    class LoginTests {

        @Test
        @DisplayName("로그인 성공 시 200(OK) 상태코드와 응답 반환 및 세션에 사용자 정보 저장")
        void login_success() throws Exception {
            LoginRequest request = new LoginRequest("test@example.com", "abcd1234");

            MemberDetails member = MemberDetails.createEmailUser(1L, "test@example.com", "hashedpassword");
            when(authService.authenticateEmail(request.email(), request.password())).thenReturn(member);

            // LoginResponse response = new LoginResponse("로그인에 성공했습니다.", 1L, "test@example.com");

            mockMvc.perform(post("/api/auth/email/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("로그인에 성공했습니다."))
                    .andExpect(jsonPath("$.email").value("test@example.com"))
                    .andExpect(jsonPath("$.userId").value(1L));
        }

        @Test
        @DisplayName("로그인 실패 시 401(UNAUTHORIZED) 상태코드와 에러 메시지 반환")
        void login_invalidCredentials() throws Exception {
            LoginRequest request = new LoginRequest("test@example.com", "wrongpassword");

            when(authService.authenticateEmail(request.email(), request.password()))
                    .thenThrow(new com.feedhanjum.back_end.auth.exception.InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다."));

            mockMvc.perform(post("/api/auth/email/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("INVALID_CREDENTIALS"))
                    .andExpect(jsonPath("$.message").value("이메일 또는 비밀번호가 올바르지 않습니다."));
        }

        @Test
        @DisplayName("유효하지 않은 입력값일 경우 400(BAD_REQUEST) 상태코드 반환")
        void login_invalidInput() throws Exception {
            LoginRequest request = new LoginRequest("", "");

            mockMvc.perform(post("/api/auth/email/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/auth/logout 테스트")
    class LogoutTests {

        @Test
        @DisplayName("로그아웃 성공 시 204(NO_CONTENT) 상태코드 반환 및 세션 무효화")
        void logout_success() throws Exception {
            mockMvc.perform(post("/api/auth/logout")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }
}
