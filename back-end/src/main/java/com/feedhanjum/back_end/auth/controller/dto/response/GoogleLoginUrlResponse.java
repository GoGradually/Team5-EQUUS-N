package com.feedhanjum.back_end.auth.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

public record GoogleLoginUrlResponse(
        @Schema(description = "구글 로그인 URL")
        @URL
        String loginUrl
) {
}
