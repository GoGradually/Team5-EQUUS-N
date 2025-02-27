package com.feedhanjum.back_end.auth.infra;

import com.feedhanjum.back_end.auth.domain.MemberDetails;
import com.feedhanjum.back_end.auth.exception.LoginStateRequiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthInterceptorTest {
    private AuthInterceptor authInterceptor;

    @BeforeEach
    void setUp() {
        authInterceptor = new AuthInterceptor();
    }

    @Test
    @DisplayName("인증되지 않은 사용자가 보호된 엔드포인트에 접근 시 preHandle은 false 반환")
    void preHandle_unauthenticatedUser() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = new Object();

        when(request.getSession(false)).thenReturn(null);

        // when, then
        assertThatThrownBy(() -> authInterceptor.preHandle(request, response, handler))
                .isInstanceOf(LoginStateRequiredException.class)
                .hasMessage("로그인이 필요합니다.");
    }

    @Test
    @DisplayName("인증된 사용자가 보호된 엔드포인트에 접근 시 preHandle은 true 반환")
    void preHandle_authenticatedUser() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = new Object();

        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionConst.MEMBER_ID)).thenReturn(MemberDetails.createEmailUser(1L, "test@example.com", "pass1234"));

        // when
        boolean result = authInterceptor.preHandle(request, response, handler);

        // then
        assertTrue(result);
    }
}