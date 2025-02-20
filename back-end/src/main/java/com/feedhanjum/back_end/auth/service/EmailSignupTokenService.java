package com.feedhanjum.back_end.auth.service;

import com.feedhanjum.back_end.auth.domain.EmailSignupToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class EmailSignupTokenService {
    private static final String TOKEN_PREFIX = "email_signup_token:";
    private final RedisTemplate<String, EmailSignupToken> redisTemplate;

    public void save(EmailSignupToken token) {
        redisTemplate.opsForValue().set(getTokenKey(token.getEmail(), token.getCode()), token, EmailSignupToken.EXPIRE_MINUTE, TimeUnit.MINUTES);
    }

    public Optional<EmailSignupToken> find(String email, String code) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(getTokenKey(email, code)));
    }

    public void delete(EmailSignupToken token) {
        redisTemplate.delete(getTokenKey(token.getEmail(), token.getCode()));
    }

    private String getTokenKey(String email, String code) {
        return TOKEN_PREFIX + email + "," + code;
    }
}
