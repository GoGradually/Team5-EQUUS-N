package com.feedhanjum.back_end.auth.infra;

import com.feedhanjum.back_end.auth.exception.LoginStateRequiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginMemberArgumentResolverTest {

    private LoginMemberArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new LoginMemberArgumentResolver();
    }

    @Test
    @DisplayName("Login 어노테이션과 Long 타입인 경우 supportsParameter가 true를 반환한다")
    void supportsParameter_true_when_loginAnnotation_and_LongType() throws Exception {
        //given
        Method method = DummyController.class.getMethod("methodWithLogin", Long.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        //when
        boolean supports = resolver.supportsParameter(parameter);
        //then
        assertThat(supports).isTrue();
    }

    @Test
    @DisplayName("Login 어노테이션이 없으면 supportsParameter가 false를 반환한다")
    void supportsParameter_false_when_no_loginAnnotation() throws Exception {
        //given
        Method method = DummyController.class.getMethod("methodWithoutLogin", Long.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        //when
        boolean supports = resolver.supportsParameter(parameter);
        //then
        assertThat(supports).isFalse();
    }

    @Test
    @DisplayName("파라미터 타입이 Long이 아니면 supportsParameter가 false를 반환한다")
    void supportsParameter_false_when_type_not_Long() throws Exception {
        //given
        Method method = DummyController.class.getMethod("methodWithLoginWrongType", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        //when
        boolean supports = resolver.supportsParameter(parameter);
        //then
        assertThat(supports).isFalse();
    }

    @Test
    @DisplayName("세션에 MEMBER_ID가 있으면 resolveArgument가 해당 값을 반환한다")
    void resolveArgument_returns_memberId_when_session_has_memberId() {
        //given
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        Object memberId = 1L;
        when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(request);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionConst.MEMBER_ID)).thenReturn(memberId);
        MethodParameter parameter = mock(MethodParameter.class);
        ModelAndViewContainer mavContainer = mock(ModelAndViewContainer.class);
        WebDataBinderFactory binderFactory = mock(WebDataBinderFactory.class);
        //when
        Object result = resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        //then
        assertThat(result).isEqualTo(memberId);
    }

    @Test
    @DisplayName("세션이 없으면 resolveArgument가 LoginStateRequiredException을 발생시킨다")
    void resolveArgument_throws_exception_when_no_session() {
        //given
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(request);
        when(request.getSession(false)).thenReturn(null);
        MethodParameter parameter = mock(MethodParameter.class);
        ModelAndViewContainer mavContainer = mock(ModelAndViewContainer.class);
        WebDataBinderFactory binderFactory = mock(WebDataBinderFactory.class);
        //when & then
        assertThatThrownBy(() -> resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory))
                .isInstanceOf(LoginStateRequiredException.class)
                .hasMessage("로그인이 필요합니다.");
    }

    @Test
    @DisplayName("세션에 MEMBER_ID가 없으면 resolveArgument가 LoginStateRequiredException을 발생시킨다")
    void resolveArgument_throws_exception_when_no_memberId_in_session() {
        //given
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(request);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionConst.MEMBER_ID)).thenReturn(null);
        MethodParameter parameter = mock(MethodParameter.class);
        ModelAndViewContainer mavContainer = mock(ModelAndViewContainer.class);
        WebDataBinderFactory binderFactory = mock(WebDataBinderFactory.class);
        //when & then
        assertThatThrownBy(() -> resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory))
                .isInstanceOf(LoginStateRequiredException.class)
                .hasMessage("로그인이 필요합니다.");
    }

    static class DummyController {
        public void methodWithLogin(@Login Long memberId) {
        }

        public void methodWithoutLogin(Long memberId) {
        }

        public void methodWithLoginWrongType(@Login String memberId) {
        }
    }
}
