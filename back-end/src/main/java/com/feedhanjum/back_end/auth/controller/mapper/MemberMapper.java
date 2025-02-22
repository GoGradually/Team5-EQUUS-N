package com.feedhanjum.back_end.auth.controller.mapper;

import com.feedhanjum.back_end.auth.controller.dto.request.MemberSignupRequest;
import com.feedhanjum.back_end.auth.controller.dto.response.MemberSignupResponse;
import com.feedhanjum.back_end.auth.domain.MemberDetails;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberDetails toEntity(MemberSignupRequest request) {
        return MemberDetails.createEmailUser(null, request.email(), request.password());
    }

    public MemberSignupResponse toResponse(MemberDetails member) {
        return new MemberSignupResponse(member.getId(), member.getEmail(), "회원가입이 완료되었습니다.");
    }
}