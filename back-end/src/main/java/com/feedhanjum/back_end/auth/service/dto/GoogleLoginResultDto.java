package com.feedhanjum.back_end.auth.service.dto;

import com.feedhanjum.back_end.auth.domain.GoogleSignupToken;
import com.feedhanjum.back_end.auth.domain.MemberDetails;
import jakarta.annotation.Nullable;

public record GoogleLoginResultDto(
        Boolean isAuthenticated,
        @Nullable
        MemberDetails memberDetails,
        @Nullable
        GoogleSignupToken googleSignupToken
) {
    public static GoogleLoginResultDto signupRequired(GoogleSignupToken googleSignupToken) {
        return new GoogleLoginResultDto(false, null, googleSignupToken);
    }

    public static GoogleLoginResultDto authenticated(MemberDetails memberDetails) {
        return new GoogleLoginResultDto(true, memberDetails, null);
    }
}
