package com.feedhanjum.back_end.auth.service;

import com.feedhanjum.back_end.auth.domain.GoogleSignupToken;
import com.feedhanjum.back_end.auth.domain.MemberDetails;
import com.feedhanjum.back_end.auth.exception.EmailAlreadyExistsException;
import com.feedhanjum.back_end.auth.exception.InvalidCredentialsException;
import com.feedhanjum.back_end.auth.passwordencoder.PasswordEncoder;
import com.feedhanjum.back_end.auth.repository.MemberDetailsRepository;
import com.feedhanjum.back_end.auth.service.dto.GoogleLoginResultDto;
import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.domain.ProfileImage;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberDetailsRepository memberDetailsRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GoogleAuthService googleAuthService;

    @Mock
    GoogleSignupTokenService googleSignupTokenService;

    @InjectMocks
    private AuthService authService;

    @Nested
    @DisplayName("registerMember 테스트")
    class RegisterMemberTests {

        @Test
        @DisplayName("이메일 중복 없으면 정상 저장")
        void registerMember_success() {
            MemberDetails inputMember = MemberDetails.createEmailUser(null, "test@example.com", "pass1234");
            String name = "홍길동";
            List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);

            when(memberDetailsRepository.findByEmail("test@example.com"))
                    .thenReturn(Optional.empty());

            Member savedMember = new Member(name, "test@example.com", null, feedbackPreferences);
            when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

            MemberDetails resultMemberDetails = MemberDetails.createEmailUser(1L, "test@example.com", "pass1234");
            when(memberDetailsRepository.save(any(MemberDetails.class))).thenReturn(resultMemberDetails);

            MemberDetails saved = authService.registerEmail(inputMember, name, null, feedbackPreferences);

            assertThat(saved).isNotNull();
            assertThat(saved.getId()).isEqualTo(1L);
            assertThat(saved.getEmail()).isEqualTo("test@example.com");
            assertThat(saved.getPassword()).isEqualTo("pass1234");

            ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
            verify(memberRepository).save(memberCaptor.capture());
            Member capturedMember = memberCaptor.getValue();
            assertThat(capturedMember.getName()).isEqualTo("홍길동");
            assertThat(capturedMember.getEmail()).isEqualTo("test@example.com");

            verify(memberDetailsRepository).save(any(MemberDetails.class));
        }

        @Test
        @DisplayName("이메일 중복 시 EmailAlreadyExistsException 발생")
        void registerMember_fail_emailAlreadyExists() {
            MemberDetails inputMember = MemberDetails.createEmailUser(null, "test@example.com", "pass1234");
            List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
            String name = "홍길동";

            when(memberDetailsRepository.findByEmail("test@example.com"))
                    .thenReturn(Optional.of(MemberDetails.createEmailUser(999L, "test@example.com", "any")));

            assertThatThrownBy(() -> authService.registerEmail(inputMember, name, null, feedbackPreferences))
                    .isInstanceOf(EmailAlreadyExistsException.class)
                    .hasMessage("이미 사용 중인 이메일입니다.");

            verify(memberRepository, never()).save(any());
            verify(memberDetailsRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("authenticate 테스트")
    class AuthenticateTests {

        @Test
        @DisplayName("인증 성공 시 MemberDetails 반환")
        void authenticate_success() {
            String email = "test@example.com";
            String password = "password";
            String hashedPassword = "password";

            MemberDetails member = MemberDetails.createEmailUser(1L, email, password);
            when(memberDetailsRepository.findByEmail(email)).thenReturn(Optional.of(member));

            when(passwordEncoder.matches(password, hashedPassword)).thenReturn(true);

            MemberDetails result = authService.authenticateEmail(email, password);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getEmail()).isEqualTo(email);
            assertThat(result.getPassword()).isEqualTo(hashedPassword);

            verify(passwordEncoder).matches(password, hashedPassword);
        }

        @Test
        @DisplayName("이메일이 존재하지 않으면 InvalidCredentialsException 발생")
        void authenticate_fail_emailNotFound() {
            String email = "nonexistent@example.com";
            String password = "pass1234";

            when(memberDetailsRepository.findByEmail(email)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> authService.authenticateEmail(email, password))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("이메일 또는 비밀번호가 올바르지 않습니다.");

            verify(passwordEncoder, never()).matches(any(), any());
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 InvalidCredentialsException 발생")
        void authenticate_fail_wrongPassword() {
            String email = "test@example.com";
            String password = "wrongpassword";
            String hashedPassword = "password"; // 예시 해시

            MemberDetails member = MemberDetails.createEmailUser(1L, email, hashedPassword);
            when(memberDetailsRepository.findByEmail(email)).thenReturn(Optional.of(member));

            when(passwordEncoder.matches(password, hashedPassword)).thenReturn(false);

            assertThatThrownBy(() -> authService.authenticateEmail(email, password))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("이메일 또는 비밀번호가 올바르지 않습니다.");

            verify(passwordEncoder).matches(password, hashedPassword);
        }
    }

    @Nested
    @DisplayName("구글 로그인 테스트")
    class GoogleLoginTest {
        @Test
        @DisplayName("구글 회원이 존재할 경우 성공")
        void test1() {
            // given
            GoogleAuthService.GoogleUserInfoResponse userInfo = new GoogleAuthService.GoogleUserInfoResponse("test@email.com", "name1");
            MemberDetails existGoogleUser = MemberDetails.createGoogleUser(1L, "test@email.com");
            String googleCode = "googleCode";
            String referer = "https://test.com";
            when(googleAuthService.getUserInfo(googleCode, referer))
                    .thenReturn(userInfo);
            when(memberDetailsRepository.findByEmail(existGoogleUser.getEmail()))
                    .thenReturn(Optional.of(existGoogleUser));

            // when
            GoogleLoginResultDto result = authService.authenticateGoogle(googleCode, referer);

            // then
            assertThat(result.isAuthenticated()).isTrue();
            assertThat(result.memberDetails())
                    .isNotNull()
                    .isEqualTo(existGoogleUser);
            assertThat(result.googleSignupToken()).isNull();
        }

        @Test
        @DisplayName("이메일 회원이 존재할 경우 실패")
        void test2() {
            // given
            GoogleAuthService.GoogleUserInfoResponse userInfo = new GoogleAuthService.GoogleUserInfoResponse("test@email.com", "name1");
            MemberDetails existEmailUser = MemberDetails.createEmailUser(1L, "test@email.com", "password");
            String googleCode = "googleCode";
            String referer = "https://test.com";
            when(googleAuthService.getUserInfo(googleCode, referer))
                    .thenReturn(userInfo);
            when(memberDetailsRepository.findByEmail(existEmailUser.getEmail()))
                    .thenReturn(Optional.of(existEmailUser));

            // when & then
            assertThatThrownBy(() -> authService.authenticateGoogle(googleCode, referer))
                    .isInstanceOf(EmailAlreadyExistsException.class);
        }

        @Test
        @DisplayName("회원이 존재하지 않을 경우 회원가입 토큰 발급")
        void test3() {
            // given
            String email = "test@email.com";
            GoogleAuthService.GoogleUserInfoResponse userInfo = new GoogleAuthService.GoogleUserInfoResponse(email, "name1");
            String googleCode = "googleCode";
            String referer = "https://test.com";


            when(googleAuthService.getUserInfo(googleCode, referer))
                    .thenReturn(userInfo);
            when(memberDetailsRepository.findByEmail(email))
                    .thenReturn(Optional.empty());

            GoogleLoginResultDto result = authService.authenticateGoogle(googleCode, referer);

            ArgumentCaptor<GoogleSignupToken> captor = ArgumentCaptor.captor();
            verify(googleSignupTokenService).save(captor.capture());
            GoogleSignupToken token = captor.getValue();
            assertThat(result.isAuthenticated()).isFalse();
            assertThat(result.memberDetails()).isNull();
            assertThat(result.googleSignupToken()).
                    isNotNull()
                    .satisfies(t -> {
                        assertThat(t.getEmail())
                                .isEqualTo(email)
                                .isEqualTo(token.getEmail());
                        assertThat(t.getCode())
                                .isEqualTo(token.getCode());
                    });
        }
    }

    @Nested
    @DisplayName("구글 회원가입 테스트")
    class GoogleSignupTest {
        @Test
        @DisplayName("이미 존재하는 이메일이면 오류")
        void test1() {
            // given
            String email = "test@email.com";
            String name = "name1";
            GoogleSignupToken token = GoogleSignupToken.generateNewToken(email, name);
            ProfileImage image = new ProfileImage("bg", "img");
            List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
            MemberDetails existGoogleUser = MemberDetails.createGoogleUser(1L, email);

            when(googleSignupTokenService.find(token.getCode())).thenReturn(Optional.of(token));
            when(memberDetailsRepository.findByEmail(email)).thenReturn(Optional.of(existGoogleUser));

            assertThatThrownBy(() -> authService.registerGoogle(token.getCode(), image, feedbackPreferences))
                    .isInstanceOf(EmailAlreadyExistsException.class);
        }

        @Test
        @DisplayName("토큰에 저장된 이메일로 가입")
        void test2() {
            // given
            String email = "test@email.com";
            String name = "name1";
            GoogleSignupToken token = GoogleSignupToken.generateNewToken(email, name);
            ProfileImage image = new ProfileImage("bg", "img");
            List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
            ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.captor();

            when(googleSignupTokenService.find(token.getCode())).thenReturn(Optional.of(token));
            when(memberDetailsRepository.findByEmail(email)).thenReturn(Optional.empty());
            when(memberRepository.save(memberCaptor.capture())).thenAnswer(i -> i.getArgument(0));
            when(memberDetailsRepository.save(any(MemberDetails.class))).thenAnswer(i -> i.getArgument(0));

            // when
            MemberDetails memberDetails = authService.registerGoogle(token.getCode(), image, feedbackPreferences);

            // then
            assertThat(memberDetails.getEmail()).isEqualTo(email);
            memberDetails.validateGoogleAccount();

            Member member = memberCaptor.getValue();
            assertThat(member.getEmail()).isEqualTo(email);
            assertThat(member.getName()).isEqualTo(name);
            assertThat(member.getProfileImage()).isEqualTo(image);
            assertThat(member.getFeedbackPreferences()).containsAll(feedbackPreferences);
        }

    }
}
