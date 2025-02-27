package com.feedhanjum.back_end.auth.infra;

import com.feedhanjum.back_end.auth.exception.LoginStateRequiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @throws LoginStateRequiredException 사용자가 로그인하지 않은 상태인 경우
 */
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true; // OPTIONS 요청은 인증 검사 없이 통과
        }
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute(SessionConst.MEMBER_ID) != null) {
            return true;
        }

        throw new LoginStateRequiredException("로그인이 필요합니다.");
    }
}
