package com.feedhanjum.back_end.auth.controller.dto.response;

import com.feedhanjum.back_end.auth.domain.GoogleSignupToken;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;

public record GoogleLoginResponse(
        @Schema(description = "로그인 처리 여부. true라면 로그인 성공, false라면 회원가입 필요")
        Boolean isAuthenticated,

        @Schema(description = "로그인이 성공했을 경우, 로그인 정보")
        @Nullable
        LoginResponse loginResponse,

        @Schema(description = "회원가입이 필요한 경우, 구글 회원가입 api 호출 시 사용할 토큰")
        @Nullable
        GoogleSignupTokenResponse googleSignupToken
) {

    public static GoogleLoginResponse authenticated(LoginResponse loginResponse) {
        return new GoogleLoginResponse(true, loginResponse, null);
    }

    public static GoogleLoginResponse signupRequired(GoogleSignupToken token) {
        return new GoogleLoginResponse(false, null,
                new GoogleSignupTokenResponse(token.getCode(), token.getExpireDate()));
    }

    public record GoogleSignupTokenResponse(
            @Schema(description = "구글 회원가입 토큰")
            @UUID(version = 4)
            String token,
            @Schema(description = "구글 회원가입 토큰 유효기간")
            @NotNull
            @Future
            LocalDateTime validUntil
    ) {

    }
}
