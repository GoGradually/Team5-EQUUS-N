package com.feedhanjum.back_end.auth.service;


import com.feedhanjum.back_end.auth.domain.EmailSignupToken;
import com.feedhanjum.back_end.auth.domain.GoogleSignupToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class GoogleSignupTokenService {
    private static final String TOKEN_PREFIX = "google_signup_token:";
    private final RedisTemplate<String, GoogleSignupToken> redisTemplate;

    public void save(GoogleSignupToken token) {
        redisTemplate.opsForValue().set(getTokenKey(token.getCode()), token, EmailSignupToken.EXPIRE_MINUTE, TimeUnit.MINUTES);
    }

    public Optional<GoogleSignupToken> find(String code) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(getTokenKey(code)));
    }

    public void delete(GoogleSignupToken token) {
        redisTemplate.delete(getTokenKey(token.getCode()));
    }

    private String getTokenKey(String code) {
        return TOKEN_PREFIX + code;
    }
}
