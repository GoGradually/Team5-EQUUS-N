package com.feedhanjum.back_end.auth.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class GoogleSignupToken {
    public static final int TOKEN_LENGTH = 36;
    public static final int EXPIRE_MINUTE = 10;
    private final String email;
    private final String name;

    @org.hibernate.validator.constraints.UUID(version = 4)
    private final String code;
    private final LocalDateTime expireDate;


    private GoogleSignupToken(String email, String name, String code) {
        this.email = email;
        this.name = name;
        this.code = code;
        expireDate = LocalDateTime.now().plusMinutes(EXPIRE_MINUTE);
    }

    private static String generateCode() {
        return UUID.randomUUID().toString();
    }

    public static GoogleSignupToken generateNewToken(String email, String name) {
        return new GoogleSignupToken(email, name, generateCode());
    }


}
